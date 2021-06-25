package com.airbnb.rentalsearch.routing

import com.airbnb.rentalprocessor.routing.Route

class Route {
    class V1 {
        companion object {
            private const val API = "/api"
            private const val VERSION = "/v1"
            private const val ADMIN = "/admin"


            // Households Search
            const val SEARCH_HOUSEHOLDS = "$API$VERSION/households/search"

            // Create New Rental Request
            const val CREAT_RENTAL_REQUEST = "$API$VERSION/rental-request/create"
        }
    }
}
