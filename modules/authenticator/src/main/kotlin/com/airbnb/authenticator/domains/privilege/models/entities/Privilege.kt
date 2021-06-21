package com.airbnb.authenticator.domains.privilege.models.entities

import com.airbnb.authenticator.common.entities.BaseEntity
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@Entity
@Table(name = "privileges", schema = "authenticator")
class Privilege : BaseEntity() {

    @Column(nullable = false, unique = true)
    lateinit var name: String

    @Column(nullable = false, unique = true)
    lateinit var label: String

    var description: String? = null

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "privileges_access_urls")
    lateinit var accessUrls: List<String>

    fun accessesArr(): Array<String> {
        return accessUrls.toTypedArray()
    }

    fun accessesStr(): String {
        return this.accessUrls.joinToString()
    }
}
