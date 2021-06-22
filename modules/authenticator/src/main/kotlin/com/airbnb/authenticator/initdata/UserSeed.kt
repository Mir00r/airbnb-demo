package com.airbnb.authenticator.initdata

import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import com.airbnb.authenticator.domains.privilege.models.enums.Privileges
import com.airbnb.authenticator.domains.privilege.services.PrivilegeService
import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.roles.models.enums.Roles
import com.airbnb.authenticator.domains.roles.repositories.RoleRepository
import com.airbnb.authenticator.domains.roles.services.RoleService
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.models.enums.Genders
import com.airbnb.authenticator.domains.users.services.UserService
import com.airbnb.authenticator.utils.PasswordUtil
import com.airbnb.common.exceptions.notfound.NotFoundException
import com.airbnb.common.utils.ExceptionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Component
class UserSeed @Autowired constructor(
    private val privilegeService: PrivilegeService,
    private val roleService: RoleService,
    private val userService: UserService
) {
    @Value("\${admin.username}")
    lateinit var adminUsername: String

    @Value("\${admin.password}")
    lateinit var adminPass: String

    @Value("\${admin.phone}")
    lateinit var adminPhone: String

    @Value("\${admin.email}")
    lateinit var adminEmail: String

    @Value("\${auth.method}")
    lateinit var authMethod: String


    @EventListener(ContextRefreshedEvent::class)
    fun onBootUp() {
        this.seedPrivileges()
    }

    private fun seedPrivileges() {
        if (!this.privilegeService.empty()) return

        val privileges = this.createPrivilegesIfNotExists()

        for (r in Roles.values()) {
            val role = Role()
            role.name = r.name

            when (r.label) {
                Roles.ADMIN.label -> {
                    role.privileges = privileges
                }
                Roles.USER.label -> {
                    role.restricted = false
                    role.privileges = getPrivilegesNameList(Privileges.ACCESS_USER_RESOURCES.name)
                }
            }
            this.roleService.save(role)
        }

        // create admin user
        val user = User()
        user.name = "Admin"
        user.username = this.adminUsername
        user.password = PasswordUtil.encryptPassword(this.adminPass, PasswordUtil.EncType.BCRYPT_ENCODER, null)
        user.phone = this.adminPhone
        user.email = this.adminEmail
        user.gender = Genders.MALE
        user.roles = ArrayList()
        user.roles.add(
            this.roleService.find(Roles.ADMIN.name)
                .orElseThrow { NotFoundException("Could not assign admin role to admin as it's not found!") })

        this.userService.save(user)
    }

    private fun createPrivilegesIfNotExists(): List<Privilege> {
        val privileges: MutableList<Privilege> = ArrayList()

        Privileges.values().forEach {
            val privilege = this.privilegeService.findByName(it.toString())
            if (!privilege.isPresent) {
                privileges.add(this.privilegeService.save(Privilege(it.name, it.label)))
            }
        }
        return privileges
    }

    private fun getPrivilegesNameList(privilegeName: String): List<Privilege> {
        val userPrivilege = this.privilegeService.findByName(privilegeName)
            .orElseThrow { ExceptionUtil.notFound("Privilege", privilegeName) }
        return listOf(userPrivilege)
    }
}
