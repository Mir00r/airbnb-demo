package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.common.exceptions.forbidden.ForbiddenException

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
interface AcValidationTokenService {

    fun save(acValidationToken: AcValidationToken): AcValidationToken
    fun findOne(id: Long): AcValidationToken

    @Throws(ForbiddenException::class)
    fun findByToken(token: String?): AcValidationToken
    fun delete(id: Long)

    @Throws(ForbiddenException::class)
    fun isTokenValid(token: String?): Boolean
    fun isLimitExceeded(user: User?): Boolean

    fun canGetOTP(username: String): Boolean
}
