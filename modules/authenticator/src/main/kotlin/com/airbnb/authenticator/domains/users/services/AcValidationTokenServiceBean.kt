package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.repositories.AcValidationTokenRepository
import com.airbnb.common.exceptions.forbidden.ForbiddenException
import com.airbnb.common.utils.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Service
class AcValidationTokenServiceBean @Autowired constructor(
    private val acValidationTokenRepository: AcValidationTokenRepository
) : AcValidationTokenService {

    override fun save(acValidationToken: AcValidationToken): AcValidationToken {
        return this.acValidationTokenRepository.save(acValidationToken)
    }

    override fun findOne(id: Long): AcValidationToken {
        return this.acValidationTokenRepository.getOne(id)
    }

    override fun findByToken(token: String?): AcValidationToken {
        if (token == null) throw ForbiddenException("Invalid Token")
        val tokenEntity: AcValidationToken = this.acValidationTokenRepository.findFirstByTokenOrderByIdDesc(token)
        if (!tokenEntity.isTokenValid()) throw ForbiddenException("Invalid Token")
        return tokenEntity
    }

    override fun delete(id: Long) {
        acValidationTokenRepository.deleteById(id)
    }

    override fun isTokenValid(token: String?): Boolean {
        if (token == null || token.isEmpty()) return false
        val acValidationToken = findByToken(token)
        return acValidationToken.isTokenValid()
    }

    override fun isLimitExceeded(user: User?): Boolean {
        if (user == null) return true
        val date = Date()
        val fromDate: Date = DateUtil.getDayStart(date)
        val toDate: Date = DateUtil.getDayEnd(date)
        return this.acValidationTokenRepository.countByUserIdAndCreatedAtBetween(
            user.id ?: 0,
            fromDate.toInstant(),
            toDate.toInstant()
        ) >= 3
    }

    override fun canGetOTP(username: String): Boolean {
        val token = this.acValidationTokenRepository.findByUserName(username)
        return if (token.isEmpty) true
        else
            token.get().isTokenValid() || Instant.now().isAfter(token.get().tokenValidUntil)
    }
}
