package com.airbnb.authenticator.domains.users.controllers

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.config.security.TokenService
import com.airbnb.authenticator.domains.users.models.dtos.TokenResponseDto
import com.airbnb.authenticator.domains.users.models.dtos.UserDto
import com.airbnb.authenticator.domains.users.models.enums.AuthMethods
import com.airbnb.authenticator.domains.users.models.mappers.TokenMapper
import com.airbnb.authenticator.domains.users.models.mappers.UserMapper
import com.airbnb.authenticator.domains.users.services.UserRegisterService
import com.airbnb.authenticator.domains.users.services.UserService
import com.airbnb.authenticator.models.UserAuth
import com.airbnb.authenticator.routing.Route
import com.airbnb.authenticator.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
@RestController
@Api(tags = [Constants.AUTHENTICATOR], description = Constants.USERS_REGISTRATION_API_DETAILS)
class UserRegisterController @Autowired constructor(
    private val userRegisterService: UserRegisterService,
    private val userMapper: UserMapper,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val tokenMapper: TokenMapper
) {

    @Value("\${token.validity}")
    lateinit var tokenValidity: String

    @PostMapping(Route.VERIFY_REGISTRATION)
    @ApiOperation(value = Constants.VERIFY_PHONE)
    fun verifyIdentity(
        @RequestParam("identity") phoneOrEmail: String,
        @RequestParam("auth_type") authType: AuthMethods
    ): ResponseEntity<TokenResponseDto> {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() + Integer.parseInt(this.tokenValidity)
        val acValidationToken =
            this.userRegisterService.requireAccountValidationByOTP(phoneOrEmail, calendar.time, authType)

        return ResponseEntity.ok(this.tokenMapper.map(acValidationToken))
    }

    @PostMapping(Route.REGISTER)
    @ApiOperation(value = Constants.REGISTER)
    fun register(
        @RequestParam("token") token: String,
        @RequestBody dto: UserDto,
        @RequestParam("auth_type") authType: AuthMethods
    ): ResponseEntity<OAuth2AccessToken> {

        val user = this.userRegisterService.register(token, this.userMapper.map(dto, null), authType)

        SecurityContext.updateAuthentication(UserAuth(user))
        return ResponseEntity.ok(tokenService.createAccessToken(UserAuth(user)))
    }


    @PostMapping(Route.CHANGE_PASSWORD)
    @ApiOperation(value = Constants.CHANGE_PASSWORD)
    fun changePassword(
        @RequestParam("current_password") currentPassword: String,
        @RequestParam("new_password") newPassword: String
    ): ResponseEntity<HttpStatus> {
        this.userRegisterService.changePassword(SecurityContext.getCurrentUser().id, currentPassword, newPassword)
        return ResponseEntity.ok().build()
    }

    // Password reset
    @GetMapping(Route.RESET_PASSWORD)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = Constants.VERIFY_RESET_PASSWORD)
    fun requestResetPassword(@RequestParam("username") username: String): ResponseEntity<TokenResponseDto> {
        val token = this.userRegisterService.handlePasswordResetRequest(username)
        return ResponseEntity.ok(this.tokenMapper.map(token))
    }

    @PostMapping(Route.RESET_PASSWORD)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = Constants.RESET_PASSWORD)
    fun resetPassword(
        @RequestParam("username") username: String,
        @RequestParam("token") token: String,
        @RequestParam("password") password: String
    ) {
        this.userRegisterService.resetPassword(username, token, password)
    }

    @PostMapping(Route.CREATE_ADMIN_ACCOUNT)
    @ApiOperation(value = Constants.POST_MSG + Constants.ADMIN)
    fun createAdminAccount(@Valid @RequestBody dto: UserDto): ResponseEntity<UserDto> {
        val entity = this.userService.save(this.userMapper.map(dto, null))
        return ResponseEntity.ok(this.userMapper.map(entity))
    }
}
