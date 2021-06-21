package com.airbnb.authenticator.utils

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
class Constants {

    companion object {
        /**
         * SWAGGER CONSTANTS
         */
        const val REST_API = "REST API"
        const val API = "API"
        const val BEARER = "Bearer "
        const val POST_MSG = "Create a new "
        const val GET_USER_MEASUREMENTS = "Fetch list of measurements for a agent "
        const val POST_BULK_MSG = "Bulk API to create "
        const val PATCH_MSG = "Update an existing "
        const val UPDATE_PATCH_MSG = "Bulk Update API an existing "
        const val GET_ALL_MSG = "Get all list of "
        const val SEARCH_ALL_MSG = "Search all list of "
        const val GENERATE_ALL_MSG = "Generate all "
        const val GET_BY_MESSAGE = "Get all by "

        // Rest API Name
        const val PRIVILEGES_ADMIN = "Privileges (Super Admin)"
        const val PRIVILEGES_ADMIN_API_DETAILS = "Admin can manipulate privileges for roles with these api's";
    }
}
