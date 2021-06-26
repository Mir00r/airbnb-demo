package com.airbnb.rentalsearch.domains.reviews_ratings.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel(description = "Model to create/update review/rating")
class ReviewDto : BaseDto() {

    @NotNull
    @JsonProperty("household_id")
    var householdId: Long = 0

    @NotBlank
    @ApiModelProperty(example = "Very Good!")
    lateinit var title: String

    @NotBlank
    @Size(max = 255)
    @ApiModelProperty(example = "This household is very good. I'm impressed.")
    lateinit var content: String


    @JsonProperty("ratings")
    lateinit var ratings: List<RatingDto>

    /*
    READONLY FIELDS
     */

    @JsonProperty("posted_by_name")
    @ApiModelProperty(readOnly = true)
    var postedByName: String? = null

    @JsonProperty("average_rating")
    @ApiModelProperty(readOnly = true)
    var averageRating: Double = 0.toDouble()

}
