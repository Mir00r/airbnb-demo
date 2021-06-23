package com.airbnb.rentalprocessor.domains.households.models.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Embeddable

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
@Embeddable
class AmenitiesDto {

    @JsonProperty("kitchen")
    var hasKitchen = false

    @JsonProperty("dining")
    var hasDining = false

    @JsonProperty("drawing")
    var hasDrawing = false

    @JsonProperty("gas")
    var hasGas = false

    @JsonProperty("water")
    var hasWater = false

    @JsonProperty("electricity")
    var hasElectricity = false

    @JsonProperty("roof_access")
    var hasRoofAccess = false

    @JsonProperty("gym")
    var hasGym = false

    @JsonProperty("swimming_pool")
    var hasSwimmingPool = false

    @JsonProperty("parking")
    var hasParking = false

    @JsonProperty("hair_dryer")
    var hasHairDryer = false

    @JsonProperty("tiled")
    var hastTiled = false

    @JsonProperty("furnished")
    var hasFurnished = false

    @JsonProperty("internet")
    var hasInternet = false

    @JsonProperty("cctv")
    var hasCCTV = false

    @JsonProperty("bbq_area")
    var hasBBQArea = false

    @JsonProperty("elevator")
    var hasElevator = false

    @JsonProperty("mosque")
    var hasMosque = false
}
