package com.airbnb.authenticator.domains.users.models

import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.users.models.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
open class UserAuth(user: User) : UserDetails {
    var id: Long? = null
    var name: String? = null
    private lateinit var authUserName: String
    private lateinit var authPassword: String
    var phone: String? = null
    var email: String? = null

    lateinit var roles: MutableList<Role>

    private var enabled = true

    private var accountNonExpired = true

    private var accountNonLocked = true

    private var credentialsNonExpired = true
    var admin = false

    fun UserAuth(user: User?) {
        requireNotNull(user) { "User can not be null!" }
        this.id = user.id
        this.name = user.name
        this.authUserName = user.username
        this.authPassword = user.password
        this.phone = user.phone
        this.email = user.email
        this.enabled = user.enabled
        this.roles = user.roles
        this.accountNonExpired = user.accountNonExpired
        this.accountNonLocked = user.accountNonLocked
        this.credentialsNonExpired = user.credentialsNonExpired
        this.admin = user.isAdmin()
    }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        if (this.roles == null) this.roles = ArrayList<Role>()
        val authorityList: MutableList<GrantedAuthority?> = ArrayList()
        for (role in this.roles!!) {
            if (role != null) {
                authorityList.addAll(
                    role.privileges.stream()
                        .map { privilege -> SimpleGrantedAuthority(privilege.name) }
                        .collect(Collectors.toList())
                )
            }
        }
        return authorityList
    }


    override fun getPassword(): String {
        return this.authPassword
    }

    override fun getUsername(): String {
        return this.authUserName
    }

    override fun isAccountNonExpired(): Boolean {
        return this.isAccountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return this.isAccountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return this.isCredentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return this.isEnabled
    }
}
