package com.airbnb.rentalprocessor.domains.households.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.enums.PropertyTypes
import com.airbnb.rentalprocessor.domains.households.models.enums.RentTypes
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


class HouseholdDto : BaseDto() {

    @NotNull
    @Min(1)
    @JsonProperty("number_of_bed")
    @ApiModelProperty(notes = "provide number of bed", example = "1", required = true)
    var numberOfBed: Byte = 1

    @NotNull
    @Min(1)
    @JsonProperty("number_of_bath")
    @ApiModelProperty(notes = "provide number of Bath", example = "1", required = true)
    var numberOfBath: Byte = 1

    @JsonProperty("number_of_balcony")
    @ApiModelProperty(notes = "provide number of balcony", example = "1")
    var numberOfBalcony: Byte = 0

    @NotNull
    @Min(100)
    @ApiModelProperty(notes = "provide size of flat", example = "100", required = true)
    var size: Long = 0

    @JsonProperty("host_name")
    @ApiModelProperty(notes = "provide number the host name of household", example = "Mr. trump")
    var hostName: String? = null

    @JsonProperty("rent_price")
    @ApiModelProperty(notes = "provide rent price of household", example = "5000", required = true)
    var rentPrice: Double = 0.toDouble()

    @JsonProperty("available_from")
    @ApiModelProperty(notes = "provide the available rent date of household", example = "2021-12-31T18:00:00.000Z")
    var availableFrom: Instant? = null

    var available: Boolean = false

    @NotNull
    @JsonProperty("rent_type")
    @ApiModelProperty(notes = "provide rent type of household", example = "PENTHOUSE", required = true)
    lateinit var rentType: RentTypes

    @NotNull
    @JsonProperty("property_type")
    @ApiModelProperty(notes = "provide property type of household", example = "COMMERCIAL", required = true)
    lateinit var propertyType: PropertyTypes

    var status: HouseholdStatuses? = null

    var address: HouseholdAddressDto? = null

    var amenities: AmenitiesDto? = null

    var images: MutableList<String>? = null
}
