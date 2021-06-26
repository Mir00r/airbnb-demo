package com.airbnb.rentalsearch.domains.reviews_ratings.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@ApiModel
class RatingDto : BaseDto() {

    @NotNull
    @Min(1)
    @Max(10)
    var value: Byte = 0

    @JsonProperty("review_id")
    @ApiModelProperty(example = "1", hidden = true)
    var reviewId: Long = 0
}
