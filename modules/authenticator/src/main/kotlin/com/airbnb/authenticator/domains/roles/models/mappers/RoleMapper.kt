package com.airbnb.authenticator.domains.roles.models.mappers

import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import com.airbnb.authenticator.domains.privilege.models.mappers.PrivilegeMapper
import com.airbnb.authenticator.domains.privilege.services.PrivilegeService
import com.airbnb.authenticator.domains.roles.models.dtos.RoleDto
import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.common.utils.ExceptionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Component
class RoleMapper @Autowired constructor(
    private val privilegeMapper: PrivilegeMapper,
    private val privilegeService: PrivilegeService
) : BaseMapper<Role, RoleDto> {

    override fun map(entity: Role): RoleDto {
        val dto = RoleDto()
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.name = entity.name
        dto.description = entity.description
        dto.restricted = entity.restricted
        dto.privileges = entity.privileges.map { privilege -> this.privilegeMapper.map(privilege) }
        return dto
    }

    override fun map(dto: RoleDto, exEntity: Role?): Role {
        val role = exEntity ?: Role()

        role.name = dto.name.replace(" ", "_").uppercase(Locale.getDefault())
        role.description = dto.description
        role.restricted = dto.restricted
        role.privileges = dto.privilegeIds.map { privilegeId ->
            this.privilegeService.find(privilegeId)
                .orElseThrow { ExceptionUtil.notFound(Privilege::class.java, privilegeId) }
        }
        return role
    }
}
