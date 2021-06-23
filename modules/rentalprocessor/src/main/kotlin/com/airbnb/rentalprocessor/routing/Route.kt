package com.airbnb.rentalprocessor.routing

class Route {
    class V1 {
        companion object {
            private const val API = "/api"
            private const val VERSION = "/v1"
            private const val ADMIN = "/admin"

            // Households
            const val CHANGE_HOUSEHOLDS_STATUS = "$API$VERSION$ADMIN/households/{id}/change/status"
            const val ADVANCE_SEARCH_HOUSEHOLDS = "$API$VERSION/households/advance"
            const val SEARCH_HOUSEHOLDS = "$API$VERSION/households"
            const val CREATE_HOUSEHOLD = "$API$VERSION/households"
            const val FIND_HOUSEHOLD = "$API$VERSION/households/{id}"
            const val UPDATE_HOUSEHOLD = "$API$VERSION/households/{id}"
            const val DELETE_HOUSEHOLD = "$API$VERSION/households/{id}"

            // Households (Web)
            const val WEB_SEARCH_HOUSEHOLDS = "/households"
            const val WEB_CREATE_HOUSEHOLD_PAGE = "/households/create"
            const val WEB_CREATE_HOUSEHOLD = "/households"
            const val WEB_FIND_HOUSEHOLD = "/households/{id}"
            const val WEB_UPDATE_HOUSEHOLD_PAGE = "/households/{id}/update"
            const val WEB_UPDATE_HOUSEHOLD = "/households/{id}"
            const val WEB_DELETE_HOUSEHOLD = "/households/{id}/delete"
        }
    }
}
