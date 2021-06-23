package com.airbnb.authenticator.domains.roles.services

import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.roles.repositories.RoleRepository
import com.airbnb.common.exceptions.exists.AlreadyExistsException
import com.airbnb.common.exceptions.invalid.InvalidException
import com.airbnb.common.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Service
class RoleServiceBean @Autowired constructor(
    private val roleRepository: RoleRepository
) : RoleService {

    override fun findUnrestricted(name: String): Optional<Role> {
        return this.roleRepository.findUnrestricted(name)
    }

    override fun findUnrestricted(): List<Role> {
        return this.roleRepository.findUnrestricted()
    }

    override fun findByIdsUnrestricted(roleIds: List<Long>): List<Role> {
        return this.roleRepository.findByRoleIdsUnrestricted(roleIds)
    }

    override fun find(name: String): Optional<Role> {
        return this.roleRepository.find(name)
    }

    override fun search(query: String, page: Int, size: Int): Page<Role> {
        return this.roleRepository.search(query, PageAttr.getPageRequest(page, size))
    }

    override fun save(entity: Role): Role {
        this.validate(entity)
        return this.roleRepository.save(entity)
    }

    override fun find(id: Long): Optional<Role> {
        return this.roleRepository.find(id)
    }

    override fun findByIds(roleIds: List<Long>): List<Role> {
        return this.roleRepository.findByRoleIds(roleIds)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        val role = this.roleRepository.find(id)
        if (!role.isPresent) throw InvalidException("Role doesn't exist with id: $id")
        this.roleRepository.delete(role.get())
    }

    override fun validate(entity: Role) {
        if (entity.isNew()) {
            if (this.roleRepository.existsByName(entity.name))
                throw AlreadyExistsException("Role exists with name: ${entity.name}")
        }
    }
}
