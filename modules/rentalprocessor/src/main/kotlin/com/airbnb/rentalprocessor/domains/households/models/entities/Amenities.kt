package com.airbnb.rentalprocessor.domains.households.models.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
@Embeddable
class Amenities : Serializable {

    @Column(name = "kitchen")
    var hasKitchen = false

    @Column(name = "dining")
    var hasDining = false

    @Column(name = "drawing")
    var hasDrawing = false

    @Column(name = "gas")
    var hasGas = false

    @Column(name = "water")
    var hasWater = false

    @Column(name = "electricity")
    var hasElectricity = false

    @Column(name = "roof_access")
    var hasRoofAccess = false

    @Column(name = "gym")
    var hasGym = false

    @Column(name = "swimming_pool")
    var hasSwimmingPool = false

    @Column(name = "parking")
    var hasParking = false

    @Column(name = "hair_dryer")
    var hasHairDryer = false

    @Column(name = "tiled")
    var hastTiled = false

    @Column(name = "furnished")
    var hasFurnished = false

    @Column(name = "internet")
    var hasInternet = false

    @Column(name = "cctv")
    var hasCCTV = false

    @Column(name = "bbq_area")
    var hasBBQArea = false

    @Column(name = "elevator")
    var hasElevator = false

    @Column(name = "mosque")
    var hasMosque = false
}
