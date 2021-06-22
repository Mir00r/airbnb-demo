package com.airbnb.authenticator.domains.roles.repositories

import com.airbnb.authenticator.domains.roles.models.entities.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.id=:id AND r.deleted=false")
    fun find(@Param("id") id: Long): Optional<Role>

    @Query("SELECT r FROM Role r WHERE r.name=:name AND r.deleted=false")
    fun find(@Param("name") name: String): Optional<Role>

    @Query("SELECT r FROM Role r WHERE r.uuid = :uuId")
    fun findByUUIDIncludingDeleted(@Param("uuId") uuId: String): Optional<Role>

    @Query("SELECT r FROM Role r WHERE r.name=:name AND r.restricted=false AND r.deleted=false")
    fun findUnrestricted(@Param("name") name: String): Optional<Role>

    @Query("SELECT r from Role r WHERE r.id IN :ids AND r.deleted=false")
    fun findByRoleIds(@Param("ids") roleIds: List<Long>): List<Role>

    @Query("SELECT r from Role r WHERE r.id IN :ids AND r.restricted=false AND r.deleted=false")
    fun findByRoleIdsUnrestricted(@Param("ids") roleIds: List<Long>): List<Role>

    fun existsByName(name: String): Boolean

    @Query("SELECT r FROM Role r WHERE r.restricted=false AND r.deleted=false")
    fun findUnrestricted(): List<Role>

    @Query(
        "select * from roles r join m_users_roles mur on r.id = mur.roles_id where r.name = :name and mur.user_id=:userId; ",
        nativeQuery = true
    )
    fun findByNameAndUserId(@Param("name") name: String, @Param("userId") userId: Long): Optional<Role>

    @Query("SELECT r FROM Role r WHERE (:q IS NULL OR r.name LIKE %:q%) AND r.deleted=false")
    fun search(@Param("q") query: String, pageable: Pageable): Page<Role>
}
