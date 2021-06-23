package com.airbnb.rentalprocessor.domains.households.models.entities

import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
@Embeddable
class HouseholdAddress {

    @Column(name = "line_one")
    lateinit var lineOne: String

    @Column(name = "line_two")
    var lineTwo: String? = null

    lateinit var country: String

    lateinit var city: String

    var state: String? = null

    var zipcode: String? = null

    @Column(name = "floor_number")
    var floorNumber: Byte = 0

    @Column(name = "latitude", nullable = false)
    var latitude: Double = 0.0

    @Column(name = "longitude", nullable = false)
    var longitude: Double = 0.0

    @Column(name = "altitude", nullable = false)
    var altitude: Double = 0.0
}
