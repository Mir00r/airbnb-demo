package com.airbnb.rentalsearch.domains.reviews_ratings.models.entities

import com.airbnb.authenticator.common.entities.BaseEntity
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
@Table(name = "reviews", schema = "rentalsearch")
class Review : BaseEntity() {

    @Column(nullable = false)
    lateinit var title: String

    @Column(nullable = false, length = 255)
    lateinit var content: String

    @Column(name = "average_rating", nullable = false)
    var averageRating: Double = 0.toDouble()

    @ManyToOne
    lateinit var household: Household

    @OneToMany(mappedBy = "review")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(CascadeType.ALL)
    var ratings: List<Rating> = ArrayList()

    @ManyToOne
    lateinit var user: User

    @PreUpdate
    fun onReviewPersistUpdate() {
        averageRating = ratings.stream()
            .mapToInt { rating -> rating.value.toInt() }
            .average().orElse(0.0)
    }
}
