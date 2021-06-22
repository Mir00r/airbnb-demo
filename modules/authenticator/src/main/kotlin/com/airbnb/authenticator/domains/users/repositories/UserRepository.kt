package com.airbnb.authenticator.domains.users.repositories;

import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.users.models.entities.User
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
interface UserRepository : JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username=:username AND u.deleted=false")
    fun find(@Param("username") username: String): Optional<User>

    @Query("SELECT u FROM User u WHERE u.id=:id AND u.deleted=false")
    fun find(@Param("id") id: Long): Optional<User>

    fun findByRolesName(role: String, pageable: Pageable): Page<User>

    fun findByUsername(username: String): Optional<User>
    fun findByPhone(phone: String): Optional<User>
    fun findByEmail(email: String): Optional<User>

    @Query("SELECT u FROM User u WHERE (:q IS NULL OR (u.username LIKE %:q% OR u.name LIKE %:q%)) AND (:role IS NULL OR :role MEMBER OF u.roles)")
    fun search(@Param("q") query: String, @Param("role") role: Role?, pageable: Pageable): Page<User>

    @Query("SELECT u FROM User u WHERE (:q IS NULL OR (u.username LIKE %:q% OR u.name LIKE %:q%))")
    fun search(@Param("q") query: String, pageable: Pageable): Page<User>

    fun findByRolesName(role: String): List<User>
}
