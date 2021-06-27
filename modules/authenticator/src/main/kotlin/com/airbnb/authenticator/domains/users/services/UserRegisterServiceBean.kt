package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.models.enums.AuthMethods
import com.airbnb.authenticator.domains.users.repositories.UserRepository
import com.airbnb.authenticator.utils.PasswordUtil
import com.airbnb.authenticator.utils.SessionIdentifierGenerator
import com.airbnb.common.exceptions.exists.UserAlreadyExistsException
import com.airbnb.common.exceptions.forbidden.ForbiddenException
import com.airbnb.common.exceptions.invalid.InvalidException
import com.airbnb.common.exceptions.limitExceed.LimitExceedException
import com.airbnb.common.exceptions.unknown.UnknownException
import com.airbnb.common.services.MailService
import com.airbnb.common.services.SmsService
import com.airbnb.common.utils.AppUtil
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.common.utils.Validator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
@Service
class UserRegisterServiceBean @Autowired constructor(
    private val userRepository: UserRepository,
    private val acValidationTokenService: AcValidationTokenService,
    private val mailService: MailService,
    private val smsService: SmsService
) : UserRegisterService {

    @Value("\${auth.method}")
    lateinit var authMethod: String

    @Value("\${applicationName}")
    lateinit var applicationName: String

    @Value("\${token.validity}")
    lateinit var tokenValidity: String

    override fun requireAccountValidationByOTP(
        phoneOrEmail: String,
        tokenValidUntil: Date,
        authType: AuthMethods
    ): AcValidationToken {
        val isPhone = this.authMethod == authType.label
        this.validateIdentity(isPhone, phoneOrEmail)

        val user = if (isPhone) this.userRepository.findByPhone(phoneOrEmail)
        else this.userRepository.findByEmail(phoneOrEmail)
        if (user.isPresent) throw UserAlreadyExistsException("User already registered with this ${authMethod}!")

        if (!this.acValidationTokenService.canGetOTP(phoneOrEmail))
            throw ForbiddenException("Already sent an OTP. Please try again in two minutes!")
        var acValidationToken = AcValidationToken()
        acValidationToken.token = SessionIdentifierGenerator.generateOTP().toString()
        acValidationToken.tokenValid = true
        acValidationToken.username = phoneOrEmail
        acValidationToken.tokenValidUntil = tokenValidUntil.toInstant()
        acValidationToken.reason = "User Registration"
        // save acvalidationtoken
        acValidationToken = this.acValidationTokenService.save(acValidationToken)
        val finalAcValidationToken = acValidationToken
        Thread {
            try {
                Thread.sleep((2 * 60 * 1000).toLong())
                finalAcValidationToken.tokenValid = false
                acValidationTokenService.save(finalAcValidationToken)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()

        // build confirmation link
        val tokenMessage = "Your " + this.applicationName + " token is: " + acValidationToken.token
        // send link by sms
        if (this.authMethod == authType.label) this.smsService.sendSms(phoneOrEmail, tokenMessage)
        else this.mailService.sendEmail(phoneOrEmail, this.applicationName + " Registration", tokenMessage)
        return acValidationToken
    }

    override fun register(token: String, user: User, authType: AuthMethods): User {
        if (!this.acValidationTokenService.isTokenValid(token))
            throw InvalidException("Token invalid!")
        val acValidationToken = this.acValidationTokenService.findByToken(token)

        val username = if (authMethod == authType.label) user.phone else user.email
        if (username != acValidationToken.username) throw InvalidException("Token invalid!")

        val savedUser = this.save(user)
        acValidationToken.tokenValid = false
        acValidationToken.reason = "Registration/Otp Confirmation"
        this.acValidationTokenService.save(acValidationToken)
        return savedUser
    }

    override fun changePassword(id: Long, currentPassword: String, newPassword: String): User {
        var user: User = this.userRepository.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }

        if (!PasswordUtil.matches(user.password, currentPassword))
            throw ForbiddenException("Password doesn't match")

        if (newPassword.length < 6) throw InvalidException("Password must be at least 6 characters!")
        user.password = PasswordUtil.encryptPassword(newPassword, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        user = this.save(user)
        return user
    }

    override fun handlePasswordResetRequest(username: String): AcValidationToken {
        val user = this.userRepository.findByUsername(username)
            .orElseThrow { ForbiddenException("Could not find user with username: $username") }

        if (this.acValidationTokenService.isLimitExceeded(user))
            throw LimitExceedException("Limit exceeded!")

        val otp = SessionIdentifierGenerator.generateOTP()

        val message = "Your Password Reset OTP code is: $otp"
        val success = this.smsService.sendSms(user.phone, message)
        // save validation token
        if (!success) throw UnknownException("Could not send SMS")

        val resetToken = AcValidationToken()
        resetToken.user = user
        resetToken.token = otp.toString()
        resetToken.tokenValid = true
        resetToken.reason = "Password Reset (Initiated)"

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() + Integer.parseInt(this.tokenValidity)
        resetToken.tokenValidUntil = calendar.time.toInstant()

        return this.acValidationTokenService.save(resetToken)
    }

    override fun resetPassword(username: String, token: String, password: String): User {
        if (password.length < 6)
            throw ForbiddenException("Password length should be at least 6")
        val acValidationToken = this.acValidationTokenService.findByToken(token)
        var user = acValidationToken.user
        if (username != user.username)
            throw ForbiddenException("You are not authorized to do this action!")
        user.password = PasswordUtil.encryptPassword(password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        acValidationToken.tokenValid = false
        acValidationToken.reason = "Password Reset"
        user = this.save(user)
        acValidationToken.user = user
        this.acValidationTokenService.save(acValidationToken)
        return user
    }

    fun save(entity: User): User {
        this.validateUserInfo(entity)
        if (entity.isNew())
            entity.password = PasswordUtil.encryptPassword(entity.password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        return this.userRepository.save(entity)
    }

    private fun validateUserInfo(entity: User) {
        if (entity.isNew() && this.userRepository.findByUsername(entity.username).isPresent) throw ExceptionUtil.forbidden(
            "User exists with username: ${entity.username}"
        )
        if (entity.roles.any { it.isAdmin() }) {
            val loggedInUser = SecurityContext.getCurrentUser()
            if (!loggedInUser.isAdmin) throw ExceptionUtil.forbidden("You are unable to create admin account")
        }
    }

    private fun validateIdentity(phone: Boolean, phoneOrEmail: String) {
        if (phone) {
            if (!Validator.isValidPhoneNumber(phoneOrEmail))
                throw InvalidException("Phone number: $phoneOrEmail is invalid!")
        } else {
            if (!AppUtil.isEmailValid(phoneOrEmail))
                throw InvalidException("Email: $phoneOrEmail is invalid!")
        }
    }
}
