package com.airbnb.rentalprocessor.domains.rentalrequests.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
enum class RequestStatuses(
    var id: Byte,
    @JsonProperty("label") val label: String
) {

    PENDING(0, "Pending"),
    CONFIRMED(1, "Confirmed"),
    VISITED(2, "Visited"),
    CANCELED(3, "Canceled");

    companion object {

        @JvmStatic
        fun get(x: Byte): RequestStatuses {
            for (g: RequestStatuses in values()) {
                if (g.id == x)
                    return g
            }
            return PENDING
        }

        @JvmStatic
        fun get(x: String): RequestStatuses {
            for (g: RequestStatuses in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return PENDING
        }
    }
}
