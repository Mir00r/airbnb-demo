package com.airbnb.authenticator.utils

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.common.utils.ExceptionUtil

/**
 * @project IntelliJ IDEA
 * @author mir00r on 24/6/21
 */
class Validation {

    companion object {
        @JvmStatic
        fun isAccessResource(createdBy: String): Boolean {
            val loggedInUser = SecurityContext.getLoggedInUsername()
            if (createdBy != loggedInUser) throw ExceptionUtil.forbidden("You are unable to access this resource")
            return true
        }
    }
}
