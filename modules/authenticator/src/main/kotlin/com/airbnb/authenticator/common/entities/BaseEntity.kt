package com.airbnb.authenticator.common.entities

import com.airbnb.authenticator.config.security.SecurityContext
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.persistence.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@MappedSuperclass
abstract class BaseEntity : Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: Instant

    @Column(name = "updated_at")
    lateinit var updatedAt: Instant

    @Column(name = "created_by")
    var createdBy: String? = null

    @Column(name = "updated_by")
    var updatedBy: String? = null

    @Column(name = "uuid_str", nullable = false, unique = true)
    var uuid: String? = null

    @Column(nullable = false)
    var deleted = false

    @PrePersist
    fun onBasePersist() {
        createdAt = Instant.now()
        updatedAt = createdAt
        createdBy = getLoggedInUsername()
        uuid = UUID.randomUUID().toString()
    }

    @PreUpdate
    fun onBaseUpdate() {
        updatedAt = Instant.now()
        updatedBy = getLoggedInUsername()
    }

    @JsonIgnore
    fun getLoggedInUsername(): String {
        return SecurityContext.getLoggedInUsername() ?: "admin"
    }

    fun isNew(): Boolean {
        return id == null
    }
}
