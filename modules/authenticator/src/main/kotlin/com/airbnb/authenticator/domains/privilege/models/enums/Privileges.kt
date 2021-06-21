package com.airbnb.authenticator.domains.privilege.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
enum class Privileges(
    var id: Byte,
    @JsonProperty("label") var label: String
) {
    ADMINISTRATION(0, "Administration"),
    ACCESS_USER_RESOURCES(1, "Access user resources");
}
