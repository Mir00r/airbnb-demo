package com.airbnb.authenticator.domains.users.models.mappers

import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.roles.models.enums.Roles
import com.airbnb.authenticator.domains.roles.services.RoleService
import com.airbnb.authenticator.domains.users.models.dtos.UserDto
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.utils.Constants
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
        val dto = UserDto();
        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.name = entity.name
            this.username = entity.username
            this.gender = entity.gender
            this.email = entity.email
            this.phone = entity.phone
            this.role = entity.roles.map { it.name }.toString()
        }
        return dto
    }

    override fun map(dto: UserDto, exEntity: User?): User {
        val entity = exEntity ?: User()
        entity.apply {
            name = dto.name
            phone = dto.phone ?: ""
            username = dto.username
            gender = dto.gender
            email = dto.email
            password = dto.password
            val r = roleService.find(dto.role).orElseThrow { ExceptionUtil.notFound(Constants.ROLE, dto.role) }
            roles = mutableListOf(r)
        }
        return entity
    }
}
