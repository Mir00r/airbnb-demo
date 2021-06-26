package com.airbnb.rentalsearch.domains.reviews_ratings.models.entities

import com.airbnb.authenticator.common.entities.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "ratings", schema = "rentalsearch")
class Rating : BaseEntity() {

    @ManyToOne
    lateinit var review: Review

    @Column(nullable = false)
    var value: Byte = 0

}
