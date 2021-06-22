package com.airbnb.authenticator.domains.roles.services

import com.airbnb.authenticator.common.services.BaseCrudService
import com.airbnb.authenticator.domains.roles.models.entities.Role
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
interface RoleService : BaseCrudService<Role> {

    fun findUnrestricted(name: String): Optional<Role>
    fun findUnrestricted(): List<Role>
    fun find(name: String): Optional<Role>
    fun findByIds(roleIds: List<Long>): List<Role>
}
