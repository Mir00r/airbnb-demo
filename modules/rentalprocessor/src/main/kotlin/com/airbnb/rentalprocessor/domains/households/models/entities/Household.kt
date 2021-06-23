package com.airbnb.rentalprocessor.domains.households.models.entities

import com.airbnb.authenticator.common.entities.BaseEntity
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.enums.PropertyTypes
import com.airbnb.rentalprocessor.domains.households.models.enums.RentTypes
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "households", schema = "rentalprocessor")
class Household : BaseEntity() {

    @Column(name = "number_of_bed")
    var numberOfBed: Byte = 1

    @Column(name = "number_of_bath")
    var numberOfBath: Byte = 1

    @Column(name = "number_of_balcony")
    var numberOfBalcony: Byte = 0

    var size: Long = 0

    @Column(name = "host_name", nullable = false)
    lateinit var hostName: String

    @Column(name = "rent_price", nullable = false)
    var rentPrice: Double = 0.toDouble()

    @Column(name = "available_from")
    var availableFrom: Instant? = null

    var available = true

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var status: HouseholdStatuses

    @Enumerated(EnumType.STRING)
    @Column(name = "rent_type", nullable = false)
    lateinit var rentType: RentTypes

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    lateinit var propertyType: PropertyTypes

    @Embedded
    var address: HouseholdAddress? = null

    @Embedded
    var amenities: Amenities? = null

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(CascadeType.ALL)
    @JoinTable(name = "household_images", schema = "rentalprocessor")
    var images: MutableList<String>? = null
}
