package com.airbnb.authenticator.domains.privilege.services

import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import com.airbnb.authenticator.domains.privilege.repositories.PrivilegeRepository
import com.airbnb.common.exceptions.exists.AlreadyExistsException
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.common.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@Service
class PrivilegeServiceBean @Autowired constructor(
    private val privilegeRepo: PrivilegeRepository
) : PrivilegeService {

    override fun empty(): Boolean {
        return this.privilegeRepo.count() == 0L
    }

    override fun findAll(): List<Privilege> {
        return this.privilegeRepo.findAll()
    }

    override fun findByName(name: String): Optional<Privilege> {
        return this.privilegeRepo.find(name)
    }

    override fun save(entity: Privilege): Privilege {
        // When creating new privilege check if already exists with same name
        this.validate(entity)
        return this.privilegeRepo.save(entity)
    }

    override fun find(id: Long): Optional<Privilege> {
        return this.privilegeRepo.find(id)
    }

    override fun search(query: String, page: Int, size: Int): Page<Privilege> {
        return this.privilegeRepo.search(query, PageAttr.getPageRequest(page, size))
    }


    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val privilege =
                this.privilegeRepo.find(id).orElseThrow { ExceptionUtil.notFound(Privilege::class.java, id) }
            privilege.deleted = true
            this.privilegeRepo.save(privilege)
            return
        }
        this.privilegeRepo.deleteById(id)
    }

    override fun validate(entity: Privilege) {
        if (entity.isNew()) {
            if (this.privilegeRepo.existsByName(entity.name)
                || this.privilegeRepo.existsByLabel(entity.label)
            )
                throw AlreadyExistsException("Privilege with same name or label already exists!")
        }
    }
}
