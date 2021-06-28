package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.roles.models.enums.Roles
import com.airbnb.authenticator.domains.roles.services.RoleService
import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.repositories.UserRepository
import com.airbnb.authenticator.routing.Route
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
    private val roleService: RoleService,
    private val mailService: MailService
) : UserService {

    @Value("\${auth.method}")
    lateinit var authMethod: String

    @Value("\${applicationName}")
    lateinit var applicationName: String

    @Value("\${token.validity}")
    lateinit var tokenValidity: String

    @Value("\${app.base-url}")
    lateinit var baseUrl: String

    override fun findAll(page: Int): Page<User> {
        return this.userRepository.findAll(PageAttr.getPageRequest(page))
    }

    override fun findByRole(role: String, page: Int): Page<User> {
        return this.userRepository.findByRolesName(role, PageAttr.getPageRequest(page))
    }

    override fun findByRole(role: String): List<User> {
        return this.userRepository.findByRolesName(role)
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

    override fun setPassword(id: Long, newPassword: String): User {
        val currentUser = SecurityContext.getCurrentUser()
        if (currentUser == null || !currentUser.isAdmin)
            throw ForbiddenException("You are not authorised to do this action.")

        val user: User = this.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }

        if (newPassword.length < 6) throw InvalidException("Password must be at least 6 characters!")
        user.password = PasswordUtil.encryptPassword(newPassword, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        return this.save(user)
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

    override fun verifyUser(id: Long): User {
        val user = this.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }
        if (user.enabled) throw ExceptionUtil.invalid("User already verified")
        user.enabled = true
        return this.userRepository.save(user)
    }

    override fun search(query: String, role: String, page: Int, size: Int): Page<User> {
        val r = this.roleService.find(role).orElseThrow { ExceptionUtil.notFound("Role", role) }
        return this.userRepository.search(query, r, PageAttr.getPageRequest(page, size))
    }

    override fun search(query: String, page: Int, size: Int): Page<User> {
        return this.userRepository.search(query, PageAttr.getPageRequest(page, size))
    }

    override fun save(entity: User): User {
        this.validate(entity)
        if (entity.isNew()) {
            entity.password = PasswordUtil.encryptPassword(entity.password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
            entity.enabled = false
        }
        val saveEntity = this.userRepository.save(entity)
        if (!saveEntity.enabled) {
            this.mailService.sendEmail(
                saveEntity.email,
                "User verified link",
                "$baseUrl${Route.VERIFIED_USER}/${saveEntity.id}"
            )
        }
        return saveEntity
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
        if (entity.roles.any { it.isAdmin() }) {
            val loggedInUser = SecurityContext.getCurrentUser()
            if (loggedInUser != null && !loggedInUser.isAdmin) throw ExceptionUtil.forbidden("You are unable to create admin account")
        }
    }
}
