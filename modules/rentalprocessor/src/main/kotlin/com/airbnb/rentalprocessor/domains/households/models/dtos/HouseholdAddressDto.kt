package com.airbnb.rentalprocessor.domains.households.models.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
class HouseholdAddressDto {

    @NotNull
    @JsonProperty("line_one")
    @ApiModelProperty(notes = "provide address one of household", example = "Address one", required = true)
    lateinit var lineOne: String

    @JsonProperty("line_two")
    @ApiModelProperty(notes = "provide address one of household", example = "Address one")
    var lineTwo: String? = null

    @NotNull
    @ApiModelProperty(notes = "provide the country name", example = "Bangladesh", required = true)
    lateinit var country: String

    @NotNull
    @ApiModelProperty(notes = "provide the city name of household", example = "Dhaka", required = true)
    lateinit var city: String

    @ApiModelProperty(notes = "provide state of household", example = "Badda")
    var state: String? = null

    @ApiModelProperty(notes = "provide the zipcode of household", example = "1212")
    var zipcode: String? = null

    @NotNull
    @JsonProperty("floor_number")
    @ApiModelProperty(notes = "provide floor number of household", example = "1", required = true)
    var floorNumber: Byte = 0

    @JsonProperty("latitude")
    @ApiModelProperty(notes = "provide latitude  one of household", example = "23.633841", required = true)
    var latitude: Double = 0.0

    @ApiModelProperty(notes = "provide longitude one of household", example = "89.066856", required = true)
    @JsonProperty("longitude")
    var longitude: Double = 0.0

    @JsonProperty("altitude")
    @ApiModelProperty(notes = "provide altitude of household", example = "10.066856", required = true)
    var altitude: Double = 0.0
}
