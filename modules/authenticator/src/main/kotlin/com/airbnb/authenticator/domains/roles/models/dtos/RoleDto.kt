package com.airbnb.authenticator.domains.roles.models.dtos

import com.airbnb.authenticator.common.dtos.BaseDto
import com.airbnb.authenticator.domains.privilege.models.dtos.PrivilegeDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@ApiModel(description = "Model to create a new User Role")
class RoleDto : BaseDto() {

    @NotBlank
    @ApiModelProperty(notes = "provide a user role name", example = "Admin", required = true)
    lateinit var name: String

    var description: String? = null

    @NotNull
    @ApiModelProperty(notes = "provide user role restriction status", example = "true", required = true)
    var restricted: Boolean = true

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var privileges: List<PrivilegeDto>? = null

    @NotNull
    @NotEmpty
    @JsonProperty(value = "privilege_ids", access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(notes = "provide list user role privileges id number", required = true)
    lateinit var privilegeIds: List<Long>
}
