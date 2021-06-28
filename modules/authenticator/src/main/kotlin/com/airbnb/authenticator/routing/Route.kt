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

        const val PUBLIC_SEARCH_USER = "$API$VERSION$PUBLIC/users"
        const val SEARCH_USER = "$API$VERSION/users"
        const val VERIFIED_USER = "$API$VERSION$PUBLIC/verify/users"
        const val CREATE_USER = "$API$VERSION$PUBLIC/users"
        const val FIND_USER = "$API$VERSION/users/{id}"
        const val UPDATE_USER = "$API$VERSION/users"
        const val DELETE_USER = "$API$VERSION/users"
        const val GET_USER_BY_USERNAME = "$API$VERSION/users/{username}"
        const val CREATE_ADMIN_USER = "$API$VERSION$ADMIN/users"

        // User registration
        const val VERIFY_REGISTRATION = "$API$VERSION/register/verify"
        const val REGISTER = "$API$VERSION/register"
        const val CHANGE_PASSWORD = "$API$VERSION/change_password"
        const val RESET_PASSWORD = "$API$VERSION/reset_password"
    }
}
