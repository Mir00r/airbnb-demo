package com.airbnb.authenticator.domains.users.models.enums

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
enum class AuthMethods(
    var id: Byte,
    var label: String
) {

    PHONE(0, "Phone"),
    EMAIL(1, "Email");

    companion object {

        @JvmStatic
        fun get(x: Byte): AuthMethods {
            for (g: AuthMethods in values()) {
                if (g.id == x)
                    return g
            }
            return EMAIL
        }

        @JvmStatic
        fun get(x: String): AuthMethods {
            for (g: AuthMethods in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return EMAIL
        }
    }
}
