package com.airbnb.authenticator.domains.privilege.models.mappers

import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.domains.privilege.models.dtos.PrivilegeDto
import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import org.springframework.stereotype.Component
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@Component
class PrivilegeMapper : BaseMapper<Privilege, PrivilegeDto> {

    override fun map(entity: Privilege): PrivilegeDto {
        val dto = PrivilegeDto()
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.label = entity.label
        dto.name = entity.name
        dto.description = entity.description
//        dto.accessUrls = entity.accessUrls
//        dto.accessUrlsArray = entity.accessUrls.toTypedArray()
        return dto
    }

    override fun map(dto: PrivilegeDto, exEntity: Privilege?): Privilege {
        val entity = exEntity ?: Privilege()

        entity.label = dto.label
        entity.name = dto.name.replace(" ", "_").uppercase(Locale.getDefault())
        entity.description = dto.description
//        entity.accessUrls = dto.accessUrls
        return entity
    }
}
