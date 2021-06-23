package com.airbnb.rentalprocessor.domains.households.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
enum class PropertyTypes(
    var id: Byte,
    @JsonProperty("label") val label: String
) {

    RESIDENTIAL(0, "Residential"),
    COMMERCIAL(1, "Commercial Space"),
    BOTH(2, "Both");

    companion object {

        @JvmStatic
        fun get(x: Byte): PropertyTypes {
            for (g: PropertyTypes in values()) {
                if (g.id == x)
                    return g
            }
            return BOTH
        }

        @JvmStatic
        fun get(x: String): PropertyTypes {
            for (g: PropertyTypes in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return BOTH
        }
    }
}
