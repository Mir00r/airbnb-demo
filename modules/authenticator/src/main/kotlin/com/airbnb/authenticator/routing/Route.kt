package com.airbnb.authenticator.routing

class Route {

    companion object {
        private const val API = "/api"
        private const val VERSION = "/v1"
        private const val ADMIN = "/admin"
        private const val PUBLIC = "/public"


        // Admin Privileges API's
        const val SEARCH_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges"
        const val CREATE_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges"
        const val FIND_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges/{id}"
        const val UPDATE_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges/{id}"
        const val DELETE_ADMIN_PRIVILEGES = "$API$VERSION$ADMIN/privileges/{id}"

        // Admin Roles API's
        const val SEARCH_ADMIN_ROLES = "$API$VERSION$ADMIN/roles"
        const val CREATE_ADMIN_ROLES = "$API$VERSION$ADMIN/roles"
        const val FIND_ADMIN_ROLES = "$API$VERSION$ADMIN/roles/{id}"
        const val UPDATE_ADMIN_ROLES = "$API$VERSION$ADMIN/roles/{id}"
        const val DELETE_ADMIN_ROLES = "$API$VERSION$ADMIN/roles/{id}"

        // Super Admin User API's
        const val SEARCH_USER_ADMIN = "$API$VERSION$ADMIN/users"
        const val FIND_USER_ADMIN = "$API$VERSION$ADMIN/users/{id}"
        const val CHANGE_USER_ADMIN_PASSWORD = "$API$VERSION$ADMIN/users/{id}/change-password"
        const val CHANGE_USER_ADMIN_ROLE = "$API$VERSION$ADMIN/users/{id}/change-role"
        const val DISABLE_USER_ADMIN = "$API$VERSION$ADMIN/users/{id}/access/toggle"

        const val PUBLIC_SEARCH_USER = "$API$VERSION/users"
        const val SEARCH_USER = "$API$VERSION/users"
        const val GET_USER_BY_USERNAME = "$API$VERSION/users/{username}"
    }
}
