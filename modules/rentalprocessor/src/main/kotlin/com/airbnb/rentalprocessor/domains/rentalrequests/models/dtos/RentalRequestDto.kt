package com.airbnb.rentalprocessor.domains.rentalrequests.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import javax.validation.constraints.NotNull


class RentalRequestDto : BaseDto() {

    @ApiModelProperty(notes = "rental request status", example = "PENDING", hidden = true)
    var status: RequestStatuses? = null
        @JsonIgnore set

    @JsonProperty("note")
    @ApiModelProperty(notes = "provide note for household host", example = "Hello World")
    var note: String? = null

    @NotNull
    @JsonProperty("requested_by_id")
    @ApiModelProperty(notes = "provide requested user id", example = "1", required = true)
    var requestedBy: Long = 0

    @NotNull
    @JsonProperty("requested_to_id")
    @ApiModelProperty(notes = "provide requested to user id", example = "1", required = true)
    var requestedTo: Long = 0

    @JsonProperty("cancelled_by_id")
    @ApiModelProperty(notes = "provide cancelled by user id", example = "1")
    var cancelledBy: Long? = null

    @JsonProperty("assigned_to_id")
    @ApiModelProperty(notes = "provide assigned user id", example = "1")
    var assignedTo: Long? = null

    @NotNull
    @JsonProperty("household_id")
    @ApiModelProperty(notes = "provide booking household id", example = "1", required = true)
    var householdId: Long = 0

    @NotNull
    @JsonProperty("check_in")
    @ApiModelProperty(notes = "provide booking check in date", example = "2021-12-31T18:00:00.000Z", required = true)
    lateinit var checkIn: Instant

    @NotNull
    @JsonProperty("check_out")
    @ApiModelProperty(notes = "provide booking check out date", example = "2022-12-31T18:00:00.000Z", required = true)
    lateinit var checkOut: Instant
}
