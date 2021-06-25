package com.airbnb.rentalprocessor.routing

class Route {
    class V1 {
        companion object {
            private const val API = "/api"
            private const val VERSION = "/v1"
            private const val ADMIN = "/admin"

            // Households
            const val CHANGE_HOUSEHOLDS_STATUS = "$API$VERSION$ADMIN/households/{id}/change/status"
            const val CHANGE_HOUSEHOLDS_AVAILABILITY = "$API$VERSION/households/{id}/change/availability"
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

            // RentalRequests
            const val ADMIN_RENTAL_REQUESTS_CHANGE_STATUS = "$API$VERSION$ADMIN/rental-requests/{id}/change/status"
            const val CANCEL_RENTAL_REQUESTS = "$API$VERSION/rental-requests/{id}/cancel"
            const val ADVANCE_SEARCH_RENTAL_REQUESTS = "$API$VERSION/rental-requests/advance"
            const val SEARCH_RENTAL_REQUESTS = "$API$VERSION/rental-requests"
            const val CREATE_RENTAL_REQUEST = "$API$VERSION/rental-requests"
            const val FIND_RENTAL_REQUEST = "$API$VERSION/rental-requests/{id}"
            const val UPDATE_RENTAL_REQUEST = "$API$VERSION/rental-requests/{id}"
            const val DELETE_RENTAL_REQUEST = "$API$VERSION/rental-requests/{id}"

            // RentalRequests (Web)
            const val WEB_SEARCH_RENTALREQUESTS = "/rentalrequests"
            const val WEB_CREATE_RENTALREQUEST_PAGE = "/rentalrequests/create"
            const val WEB_CREATE_RENTALREQUEST = "/rentalrequests"
            const val WEB_FIND_RENTALREQUEST = "/rentalrequests/{id}"
            const val WEB_UPDATE_RENTALREQUEST_PAGE = "/rentalrequests/{id}/update"
            const val WEB_UPDATE_RENTALREQUEST = "/rentalrequests/{id}"
            const val WEB_DELETE_RENTALREQUEST = "/rentalrequests/{id}/delete"
        }
    }
}
