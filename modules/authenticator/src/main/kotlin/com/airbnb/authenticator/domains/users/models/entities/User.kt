package com.airbnb.authenticator.domains.users.models.entities

import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.users.models.UserAuth
import com.airbnb.authenticator.domains.users.models.enums.Genders
import com.airbnb.authenticator.common.entities.BaseEntity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@Entity
@Table(name = "m_users", schema = "authenticator")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class User : BaseEntity {

    @Column(nullable = false)
    lateinit var name: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var gender: Genders

    @Column(unique = true, nullable = false)
    lateinit var username: String

    @Column(unique = true)
    var email: String? = null

    @Column(unique = true)
    var phone: String? = null

    @Column(length = 512, nullable = false)
    lateinit var password: String

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "m_users_roles",
        schema = "authenticator",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "roles_id", referencedColumnName = "id")]
    )
    lateinit var roles: MutableList<Role>
    var enabled = true

    @Column(name = "account_non_expired")
    var accountNonExpired = true

    @Column(name = "account_non_locked")
    var accountNonLocked = true

    @Column(name = "credentials_non_expired")
    var credentialsNonExpired = true

    constructor() {}
    constructor(auth: UserAuth?) {
        requireNotNull(auth) { "User can not be null!" }
        this.id = auth.id
        name = auth.name ?: ""
        username = auth.username
        password = auth.password
        phone = auth.phone
        email = auth.email
        enabled = auth.isEnabled
        roles = auth.roles
        accountNonExpired = auth.isAccountNonExpired
        accountNonLocked = auth.isAccountNonLocked
        credentialsNonExpired = auth.isCredentialsNonExpired
    }

    fun grantRole(role: Role) {
        // check if user already has that role
        if (!hasRole(role) && !role.isAdmin()) roles.add(role)
    }

    fun hasRole(role: Role?): Boolean {
        return roles.stream().anyMatch { r: Role ->
            r.isSameAs(
                role
            )
        }
    }

    fun isAdmin(): Boolean {
        return roles.stream().anyMatch(Role::isAdmin)
    }

    fun canLogin(): Boolean {
        return (enabled
                && accountNonExpired
                && accountNonLocked
                && credentialsNonExpired)
    }
}
