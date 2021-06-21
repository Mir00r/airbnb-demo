package com.airbnb.authenticator.domains.users.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
enum class Genders(
    var id: Byte,
    var label: String
) {

    MALE(0, "Male"),
    FEMALE(1, "Female"),
    OTHER(2, "Other"),
    UNKNOWN(3, "Unknown");

    companion object {

        @JvmStatic
        fun get(x: Byte): Genders {
            for (g: Genders in values()) {
                if (g.id == x)
                    return g
            }
            return UNKNOWN
        }

        @JvmStatic
        fun get(x: String): Genders {
            for (g: Genders in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return UNKNOWN
        }
    }
}
