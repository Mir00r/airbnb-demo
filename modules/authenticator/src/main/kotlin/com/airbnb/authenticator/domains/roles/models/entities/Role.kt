package com.airbnb.authenticator.domains.roles.models.entities

import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import com.airbnb.authenticator.domains.privilege.models.enums.Privileges
import com.airbnb.authenticator.common.entities.BaseEntity
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@Entity
@Table(name = "roles", schema = "authenticator")
class Role : BaseEntity() {

    @Column(nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description")
    var description: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
        name = "roles_privileges",
        schema = "authenticator",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    lateinit var privileges: List<Privilege>

    @Column(nullable = false)
    var restricted = true

    fun isAdmin(): Boolean {
        return privileges.stream().anyMatch { privilege: Privilege ->
            Privileges.ADMINISTRATION.name == privilege.name
        }
    }

    fun isSameAs(role: Role?): Boolean {
        return role?.id?.equals(this.id) ?: false
    }

    fun hasPrivilege(privilegeId: Long?): Boolean {
        return if (privilegeId == null) false else privileges.stream().anyMatch { p: Privilege ->
            p.id?.equals(privilegeId) ?: false
        }
    }
}
