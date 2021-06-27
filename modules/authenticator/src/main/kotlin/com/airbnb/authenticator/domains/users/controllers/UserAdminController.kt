package com.airbnb.authenticator.domains.users.controllers

import com.airbnb.authenticator.config.security.TokenService
import com.airbnb.authenticator.domains.users.models.dtos.UserDto
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.models.mappers.UserMapper
import com.airbnb.authenticator.domains.users.services.UserService
import com.airbnb.authenticator.models.UserAuth
import com.airbnb.authenticator.routing.Route
import com.airbnb.authenticator.utils.Constants
import com.airbnb.common.utils.ExceptionUtil
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @project IntelliJ IDEA
 * @author mir00r on 26/6/21
 */
@RestController
@Api(tags = [Constants.USERS_ADMIN], description = Constants.USERS_ADMIN_API_DETAILS)
class UserAdminController @Autowired constructor(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val tokenService: TokenService
) {

    @GetMapping(Route.SEARCH_USER_ADMIN)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.USERS)
    fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam(value = "role", defaultValue = "USER") role: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<UserDto>> {

        val userPage = this.userService.search(query, role, page, size)
        return ResponseEntity.ok(userPage.map { this.userMapper.map(it) })
    }

    @GetMapping(Route.FIND_USER_ADMIN)
    @ApiOperation(value = Constants.GET_MSG + Constants.USERS + Constants.BY_ID_MSG)
    fun getUser(@PathVariable("id") userId: Long): ResponseEntity<Any> {
        val user = this.userService.find(userId).orElseThrow { ExceptionUtil.notFound(User::class.java, userId) }
        return ResponseEntity.ok(this.userMapper.map(user))
    }

    @PostMapping(Route.CREATE_ADMIN_USER)
    @ApiOperation(value = Constants.POST_MSG + " admin " + Constants.USERS)
    fun createAdmin(@Valid @RequestBody dto: UserDto): ResponseEntity<UserDto> {
        val entity = this.userService.save(this.userMapper.map(dto, null))
        return ResponseEntity.ok(this.userMapper.map(entity))
    }

    @PostMapping(Route.DISABLE_USER_ADMIN)
    @ApiOperation(value = "Disable user by id")
    fun disableUser(
        @PathVariable("id") id: Long,
        @RequestParam("enabled") enabled: Boolean
    ): ResponseEntity<Any> {
        var user = this.userService.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }
        user.enabled = enabled
        user = this.userService.save(user)
        this.tokenService.revokeAuthentication(UserAuth(user))
        return ResponseEntity.ok(this.userMapper.map(user))
    }

    @PutMapping(Route.CHANGE_USER_ADMIN_ROLE)
    @ApiOperation(value = "Change user role by id")
    fun changeRole(
        @PathVariable("id") id: Long,
        @RequestParam("roles") roles: List<Long>
    ): ResponseEntity<*> {
        val user = this.userService.setRoles(id, roles)
        return ResponseEntity.ok(this.userMapper.map(user))
    }

    @PatchMapping(Route.CHANGE_USER_ADMIN_PASSWORD)
    @ApiOperation(value = "Change user password")
    fun changePassword(
        @PathVariable("id") userId: Long,
        @RequestParam("newPassword") newPassword: String
    ): ResponseEntity<Any> {
        this.userService.setPassword(userId, newPassword)
        return ResponseEntity.ok().build()
    }
}
