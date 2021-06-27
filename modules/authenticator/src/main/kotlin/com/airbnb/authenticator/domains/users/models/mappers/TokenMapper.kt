package com.airbnb.authenticator.domains.users.models.mappers

import com.airbnb.authenticator.domains.users.models.dtos.TokenResponseDto
import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
@Component
class TokenMapper {

    @Value("\${token.validity}")
    private val tokenValidity: String? = null

    fun map(token: AcValidationToken): TokenResponseDto {
        val dto = TokenResponseDto()
        dto.apply {
            username = token.username
            tokenValidUntil = token.tokenValidUntil
            tokenValidityMillis = tokenValidity!!.toLong()
        }
        return dto
    }
}
