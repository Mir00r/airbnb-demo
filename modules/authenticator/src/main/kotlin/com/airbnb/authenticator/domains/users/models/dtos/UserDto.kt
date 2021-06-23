package com.airbnb.authenticator.domains.users.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.airbnb.authenticator.domains.users.models.enums.Genders
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
class UserDto : BaseDto() {

    @NotBlank
    @JsonProperty("name")
    @ApiModelProperty(notes = "provide user's name", example = "Donald", required = true)
    lateinit var name: String

    @NotBlank
    @Size(min = 3)
    @ApiModelProperty(notes = "provide a unique username", example = "trump", required = true)
    lateinit var username: String

    @ApiModelProperty(notes = "provide division name in english language", example = "01914837383")
    var phone: String? = null

    @Email
    @NotBlank
    @ApiModelProperty(notes = "provide unique email address", example = "sample@yahoo.com")
    lateinit var email: String

    @NotBlank
    @Size(min = 5, message = "Password length should be 5 character!")
    @ApiModelProperty(notes = "provide a user password with length at least 5 character", example = "trump", required = true)
    lateinit var password: String
        @JsonIgnore get
        @JsonProperty("password") set

    @NotNull
    @JsonProperty("gender")
    @ApiModelProperty(notes = "provide user profile gender type enum id number", example = "MALE", required = true)
    lateinit var gender: Genders

    @NotBlank
    @JsonProperty("role")
    @ApiModelProperty(notes = "provide user role type", example = "USER", required = true)
    lateinit var role: String
}
