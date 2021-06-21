package com.airbnb.authenticator.routing

class Route {

    companion object {
        private const val API = "/api"
        private const val VERSION = "/v1"
        private const val ADMIN = "/admin"


        // Admin Privileges API's
        const val SEARCH_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges"
        const val CREATE_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges"
        const val FIND_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges/{id}"
        const val UPDATE_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges/{id}"
        const val DELETE_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges/{id}"
    }
}
