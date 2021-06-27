package com.airbnb.authenticator.domains.users.repositories

import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Repository
interface AcValidationTokenRepository : JpaRepository<AcValidationToken, Long> {
    fun findFirstByTokenOrderByIdDesc(token: String): AcValidationToken
    fun findFirstByUsernameOrderByIdDesc(phone: String): AcValidationToken
    fun countByUserIdAndCreatedAtBetween(id: Long, fromDate: Instant, toDate: Instant): Int

    @Query("SELECT ac FROM AcValidationToken ac WHERE ac.username=:username AND ac.deleted=false ")
    fun findByUserName(username: String): Optional<AcValidationToken>
}
