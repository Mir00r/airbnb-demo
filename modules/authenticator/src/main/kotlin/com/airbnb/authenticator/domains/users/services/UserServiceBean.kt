package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.roles.models.enums.Roles
import com.airbnb.authenticator.domains.roles.services.RoleService
import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.repositories.UserRepository
import com.airbnb.authenticator.utils.PasswordUtil
import com.airbnb.authenticator.utils.SessionIdentifierGenerator
import com.airbnb.common.exceptions.exists.UserAlreadyExistsException
import com.airbnb.common.exceptions.forbidden.ForbiddenException
import com.airbnb.common.exceptions.invalid.InvalidException
import com.airbnb.common.exceptions.limitExceed.LimitExceedException
import com.airbnb.common.exceptions.notfound.NotFoundException
import com.airbnb.common.exceptions.unknown.UnknownException
import com.airbnb.common.services.MailService
import com.airbnb.common.services.SmsService
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.common.utils.PageAttr
import com.airbnb.common.utils.Validator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Service
open class UserServiceBean @Autowired constructor(
    private val userRepository: UserRepository,
    private val acValidationTokenService: AcValidationTokenService,
    private val mailService: MailService,
    private val smsService: SmsService,
    private val roleService: RoleService
) : UserService {

    @Value("\${auth.method}")
    lateinit var authMethod: String

    @Value("\${applicationName}")
    lateinit var applicationName: String

    @Value("\${token.validity}")
    lateinit var tokenValidity: String

    override fun findAll(page: Int): Page<User> {
        return this.userRepository.findAll(PageAttr.getPageRequest(page))
    }

    override fun findByRole(role: String, page: Int): Page<User> {
        return this.userRepository.findByRolesName(role, PageAttr.getPageRequest(page))
    }

    override fun findByRole(role: String): List<User> {
        return this.userRepository.findByRolesName(role)
    }

    override fun register(token: String, user: User): User {
        if (!this.acValidationTokenService.isTokenValid(token))
            throw InvalidException("Token invalid!")
        val acValidationToken = this.acValidationTokenService.findByToken(token)

        val username = if (authMethod == "phone") user.phone else user.email
        if (username != acValidationToken.username) throw InvalidException("Token invalid!")

        val savedUser = this.save(user)
        acValidationToken.tokenValid = false
        acValidationToken.reason = "Registration/Otp Confirmation"
        this.acValidationTokenService.save(acValidationToken)
        this.notifyAdmin(savedUser)
        return savedUser
    }

    override fun requireAccountValidationByOTP(phoneOrEmail: String, tokenValidUntil: Date): Boolean {
        val isPhone = this.authMethod == "phone"
        this.validateIdentity(isPhone, phoneOrEmail)

        val user = if (isPhone) this.userRepository.findByPhone(phoneOrEmail)
        else this.userRepository.findByEmail(phoneOrEmail)
        if (user.isPresent) throw UserAlreadyExistsException("User already registered with this ${authMethod}!")

        if (!this.acValidationTokenService.canGetOTP(phoneOrEmail))
            throw ForbiddenException("Already sent an OTP. Please try agin in two minutes!")
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
        return if (this.authMethod == "phone") this.smsService.sendSms(phoneOrEmail, tokenMessage)
        else this.mailService.sendEmail(phoneOrEmail, this.applicationName + " Registration", tokenMessage)
    }

    override fun findByUsername(username: String): Optional<User> {
        return this.userRepository.findByUsername(username)
    }

    override fun findByPhone(phone: String): Optional<User> {
        return this.userRepository.findByPhone(phone)
    }

    override fun findByEmail(email: String): Optional<User> {
        return this.userRepository.findByEmail(email)
    }

    override fun changePassword(id: Long, currentPassword: String, newPassword: String): User {
        var user: User = this.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }

        if (!PasswordUtil.matches(user.password, currentPassword))
            throw ForbiddenException("Password doesn't match")

        if (newPassword.length < 6) throw InvalidException("Password must be at least 6 characters!")
        user.password = PasswordUtil.encryptPassword(newPassword, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        user = this.save(user)
        return user
    }

    override fun setPassword(id: Long, newPassword: String): User {
        val currentUser = SecurityContext.getCurrentUser()
        if (currentUser == null || !currentUser.admin)
            throw ForbiddenException("You are not authorised to do this action.")

        val user: User = this.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }

        if (newPassword.length < 6) throw InvalidException("Password must be at least 6 characters!")
        user.password = PasswordUtil.encryptPassword(newPassword, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        return this.save(user)
    }

    override fun handlePasswordResetRequest(username: String) {
        val user = this.findByUsername(username)
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

        this.acValidationTokenService.save(resetToken)
    }

    override fun setRoles(id: Long, roleIds: List<Long>): User {
        val user = this.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }
        val isAdmin = user.isAdmin() // check if user is admin
        val roles = this.roleService.findByIds(roleIds)
        user.roles = roles.filter { role -> !role.isAdmin() }.toMutableList()
        if (isAdmin) {// set admin role explicitly after clearing roles
            val role =
                this.roleService.find(Roles.ADMIN.name).orElseThrow { NotFoundException("Admin role couldn't be set!") }
            user.roles.add(role)
        }
        return this.save(user)
    }

    @Transactional
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

    override fun search(query: String, page: Int, size: Int): Page<User> {
        return this.userRepository.search(query, PageAttr.getPageRequest(page, size))
    }

    override fun save(entity: User): User {
        this.validate(entity)
        return this.userRepository.save(entity)
    }

    override fun find(id: Long): Optional<User> {
        return this.userRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val user =
                this.userRepository.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }
            user.deleted = true
            this.userRepository.save(user)
            return
        }
        this.userRepository.deleteById(id)
    }

    override fun userExists(username: String): Boolean {
        return this.findByUsername(username).isPresent
    }

    override fun validate(entity: User) {
        if (entity.isNew() && this.userExists(entity.username)) throw ExceptionUtil.forbidden("User exists with username: ${entity.username}")
    }

    private fun validateIdentity(phone: Boolean, phoneOrEmail: String) {
        if (phone) {
            if (!Validator.isValidPhoneNumber(phoneOrEmail))
                throw InvalidException("Phone number: $phoneOrEmail is invalid!")
        } else {
            if (!Validator.isValidEmail(phoneOrEmail))
                throw InvalidException("Email: $phoneOrEmail is invalid!")
        }
    }

    private fun notifyAdmin(user: User) {
//        val data = NotificationData()
//        data.title = "New Registration -:- " + user.name
//        val description = "Username: " + user.username + ", On: " + DateUtil.getReadableDateTime(Date())
//        val brief = description.substring(0, Math.min(description.length, 100))
//        data.message = brief
//        data.type = PushNotification.Type.ADMIN_NOTIFICATIONS.value
//
//        val notification = PushNotification(null, data)
//        notification.to = "/topics/adminnotifications"
//        this.notificationService.sendNotification(notification)
    }
}
