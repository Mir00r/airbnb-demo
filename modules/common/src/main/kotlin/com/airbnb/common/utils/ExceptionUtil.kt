package com.airbnb.common.utils

import com.airbnb.common.exceptions.exists.AlreadyExistsException
import com.airbnb.common.exceptions.forbidden.ForbiddenException
import com.airbnb.common.exceptions.invalid.InvalidException
import com.airbnb.common.exceptions.notfound.NotFoundException

/**
 * @project IntelliJ IDEA
 * @author mir00r on 21/6/21
 */
class ExceptionUtil {

    companion object {
        @JvmStatic
        val MSG_UNAUTHORIZED = "You're not authorized to access this resource."

        fun forbidden(message: String): ForbiddenException {
            return ForbiddenException(message)
        }

        fun notFound(message: String): NotFoundException {
            return NotFoundException(message)
        }

        fun notFound(entityName: String, id: Long): NotFoundException {
            return NotFoundException("Could not find $entityName with id: $id")
        }

        fun notFound(klass: Class<*>, id: Long): NotFoundException {
            return NotFoundException("Could not find ${klass.simpleName} with id: $id")
        }

        fun invalid(message: String): InvalidException {
            return InvalidException(message)
        }

        fun wtf(message: String): RuntimeException {
            return RuntimeException(message)
        }

        fun exists(message: String): AlreadyExistsException {
            return AlreadyExistsException(message)
        }

    }
}
