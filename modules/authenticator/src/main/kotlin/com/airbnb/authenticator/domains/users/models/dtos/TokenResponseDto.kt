package com.airbnb.authenticator.domains.users.models.dtos

import io.swagger.annotations.ApiModelProperty
import org.codehaus.jackson.annotate.JsonProperty
import java.time.Instant

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
class TokenResponseDto {

    @ApiModelProperty(readOnly = true)
    @JsonProperty("username")
    var username: String? = null

    @ApiModelProperty(readOnly = true)
    @JsonProperty("token_valid_until")
    var tokenValidUntil: Instant? = null

    @ApiModelProperty(readOnly = true)
    @JsonProperty("token_validity_millis")
    var tokenValidityMillis: Long = 0
}
