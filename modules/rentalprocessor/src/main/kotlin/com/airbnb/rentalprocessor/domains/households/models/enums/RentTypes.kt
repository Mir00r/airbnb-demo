package com.airbnb.rentalprocessor.domains.households.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/6/21
 */
enum class RentTypes(
    var id: Byte,
    @JsonProperty("label") val label: String
) {

    BACHELOR(0, "Bachelor"),
    SUBLET(1, "Sublet"),
    FAMILY(2, "Family"),
    OFFICE(3, "Office Space"),
    COMMERCIAL(4, "Commercial Space"),
    APARTMENT(5, "Apartment"),
    PENTHOUSE(6, "Penthouse"),
    DUPLEX(7, "Duplex"),
    PLAZA(8, "Plaza"),
    BUILDING(9, "Building"),
    ANY(10, "Any");

    companion object {

        @JvmStatic
        fun get(x: Byte): RentTypes {
            for (g: RentTypes in values()) {
                if (g.id == x)
                    return g
            }
            return ANY
        }

        @JvmStatic
        fun get(x: String): RentTypes {
            for (g: RentTypes in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return ANY
        }
    }
}
