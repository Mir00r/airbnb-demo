package com.airbnb.authenticator.domains.roles.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
enum class Roles(
    var id: Byte,
    var label: String
) {

    ADMIN(0, "Admin"),
    USER(1, "User");

    companion object {

        @JvmStatic
        fun get(x: Byte): Roles {
            for (g: Roles in values()) {
                if (g.id == x)
                    return g
            }
            return USER
        }

        @JvmStatic
        fun get(x: String): Roles {
            for (g: Roles in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return USER
        }
    }
}
