package com.airbnb.authenticator.domains.privilege.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
@ApiModel(description = "Model to create a new User Privilege")
class PrivilegeDto : BaseDto() {
    @NotBlank
    @ApiModelProperty(notes = "provide the privilege name of user", example = "Update Post", required = true)
    lateinit var name: String

    var description: String? = null

    @NotBlank
    @ApiModelProperty(notes = "provide the privilege level of user", example = "Create Post", required = true)
    lateinit var label: String

//    @NotBlank.List
//    @JsonProperty("access_urls")
//    @ApiModelProperty(
//        notes = "provide the list of privilege url user can access",
//        example = "[\"change-password\"]",
//        required = true
//    )
//    lateinit var accessUrls: List<String>
//
//    lateinit var accessUrlsArray: Array<String>
}
