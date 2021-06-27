package com.airbnb.authenticator.domains.users.models.entities

import com.airbnb.authenticator.common.entities.ValidationToken
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@Entity
@Table(name = "ac_validation_tokens", schema = "authenticator")
class AcValidationToken : ValidationToken() {

    @OneToOne
    lateinit var user: User
    var reason: String? = null
    lateinit var username: String
}
