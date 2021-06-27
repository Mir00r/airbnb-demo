package com.airbnb.authenticator.domains.users.services

import com.airbnb.authenticator.domains.users.models.entities.AcValidationToken
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.models.enums.AuthMethods
import java.util.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
interface UserRegisterService {
    fun requireAccountValidationByOTP(phoneOrEmail: String, tokenValidUntil: Date, authType: AuthMethods): AcValidationToken
    fun register(token: String, user: User, authType: AuthMethods): User
    fun changePassword(id: Long, currentPassword: String, newPassword: String): User
    fun handlePasswordResetRequest(username: String): AcValidationToken
    fun resetPassword(username: String, token: String, password: String): User
}
