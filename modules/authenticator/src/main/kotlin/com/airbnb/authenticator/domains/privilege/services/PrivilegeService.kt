package com.airbnb.authenticator.domains.privilege.services

import com.airbnb.authenticator.common.services.BaseCrudService
import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
interface PrivilegeService : BaseCrudService<Privilege> {

    fun empty(): Boolean
    fun findAll(): List<Privilege>
    fun findByName(name: String): Optional<Privilege>
}
