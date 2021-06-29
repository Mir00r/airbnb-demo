package com.airbnb.common.models

/**
 * @project IntelliJ IDEA
 * @author mir00r on 29/6/21
 */
class ErrorResponse {

    var code: Int = 0
    var status: String
    var message: String

    var exception: Throwable? = null

    constructor(code: Int, status: String, message: String) {
        this.code = code
        this.status = status
        this.message = message
    }

    constructor(code: Int, status: String, message: String, exception: Throwable?) {
        this.code = code
        this.status = status
        this.message = message
        this.exception = exception
    }
}
