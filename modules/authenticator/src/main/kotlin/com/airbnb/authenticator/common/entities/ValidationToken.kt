package com.airbnb.authenticator.common.entities

import java.time.Instant
import java.util.*
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@MappedSuperclass
open class ValidationToken : BaseEntity() {
    var token: String? = null
    var tokenValid: Boolean = false
    var tokenValidUntil: Instant? = null

    fun isTokenValid(): Boolean {
        return if (tokenValidUntil == null) false else tokenValid && !Instant.now().isAfter(tokenValidUntil)
    }
}
