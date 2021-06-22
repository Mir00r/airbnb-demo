package com.airbnb.authenticator.domains.users.models.mappers

import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.roles.services.RoleService
import com.airbnb.authenticator.domains.users.models.dtos.UserDto
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.utils.PasswordUtil
import com.airbnb.common.utils.ExceptionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Component
class UserMapper @Autowired constructor(
    private val roleService: RoleService
) : BaseMapper<User, UserDto> {

    override fun map(entity: User): UserDto {
        TODO("Not yet implemented")
    }

    override fun map(dto: UserDto, exEntity: User?): User {
        val entity = exEntity ?: User()
        entity.apply {
            name = dto.name
            phone = dto.phone
            username = dto.username

            if (dto.password.isNotBlank()) {
                if (dto.password.length < 6) throw ExceptionUtil.forbidden("Invalid password length!")
                password = PasswordUtil.encryptPassword(dto.password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
            } else if (exEntity == null) // if password not entered for new user, throw exception
                throw ExceptionUtil.forbidden("Password length not be empty!")

            email = dto.email
            if (exEntity == null || !exEntity.isAdmin)
                roles = if (SecurityContext.getCurrentUser().isAdmin) roleService.findByIds(dto.roleIds) else roleService.findByIdsUnrestricted(dto.roleIds)
        }
        return entity
    }
}
