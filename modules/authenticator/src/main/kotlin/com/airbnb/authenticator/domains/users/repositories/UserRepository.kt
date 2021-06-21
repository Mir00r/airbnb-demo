package com.airbnb.authenticator.domains.users.repositories;

import com.airbnb.authenticator.domains.users.models.entities.User
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
}
