package com.airbnb.common.handler

import com.airbnb.common.exceptions.forbidden.ForbiddenException
import com.airbnb.common.exceptions.invalid.InvalidException
import com.airbnb.common.exceptions.notfound.NotFoundException
import com.airbnb.common.models.ErrorResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.RuntimeException

/**
 * @project IntelliJ IDEA
 * @author mir00r on 29/6/21
 */
@ControllerAdvice
class BaseExceptionHandler @Autowired constructor(
    private val env: Environment
) {

    fun debug(): Boolean {
        val profiles = env.activeProfiles
        return !profiles.contains("prod")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(ex: ForbiddenException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.FORBIDDEN, ex)
    }

    @ExceptionHandler(InvalidException::class)
    fun handleInvalidException(ex: InvalidException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.BAD_REQUEST, ex)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.EXPECTATION_FAILED, ex)
    }

    fun buildResponse(status: HttpStatus, ex: Throwable): ResponseEntity<ErrorResponse> {
        val response = if (debug()) ErrorResponse(
            status.value(),
            status.name,
            ex.message ?: "",
            ex
        ) else ErrorResponse(
            status.value(),
            status.name,
            ex.message ?: ""
        )

        return ResponseEntity
            .status(status)
            .body(response)
    }
}
