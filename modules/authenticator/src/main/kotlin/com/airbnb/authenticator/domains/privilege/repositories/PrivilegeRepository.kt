package com.airbnb.authenticator.domains.privilege.repositories;

import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author mir00r on 21/6/21
 * @project IntelliJ IDEA
 */
@Repository
interface PrivilegeRepository : JpaRepository<Privilege, Long> {

    @Query("SELECT p FROM Privilege p WHERE (:q IS NULL OR p.name LIKE %:q%) AND p.deleted=false")
    fun search(@Param("q") query: String, pageable: Pageable): Page<Privilege>

    @Query("SELECT p FROM Privilege p WHERE p.id=:id AND p.deleted = false")
    fun find(@Param("id") id: Long): Optional<Privilege>

    @Query("SELECT p FROM Privilege p WHERE p.name=:name AND p.deleted = false")
    fun find(@Param("name") name: String): Optional<Privilege>

    fun existsByName(name: String): Boolean
    fun existsByLabel(label: String): Boolean
}
