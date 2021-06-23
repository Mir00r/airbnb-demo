package com.airbnb.rentalprocessor.domains.households.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
enum class HouseholdStatuses(
    var id: Byte,
    @JsonProperty("label") val label: String
) {

    PENDING(0, "Pending"),
    ACCEPTED(1, "Accepted"),
    REJECTED(2, "Rejected");

    companion object {

        @JvmStatic
        fun get(x: Byte): HouseholdStatuses {
            for (g: HouseholdStatuses in values()) {
                if (g.id == x)
                    return g
            }
            return REJECTED
        }

        @JvmStatic
        fun get(x: String): HouseholdStatuses {
            for (g: HouseholdStatuses in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return REJECTED
        }
    }
}
