package com.airbnb.authenticator.domains.users.controllers

import com.airbnb.authenticator.domains.users.services.UserService
import com.airbnb.authenticator.routing.Route
import com.airbnb.authenticator.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/6/21
 */
@RestController
@Api(tags = [Constants.USERS_ADMIN], description = Constants.USERS_ADMIN_API_DETAILS)
class UserController @Autowired constructor(
    private val userService: UserService,
    private val usermap
) {
    @GetMapping(Route.SEARCH_USER_ADMIN)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.USERS)
    fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam(value = "role", defaultValue = "User") role: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<UserDto>> {

        val userPage = this.userService.search(query, role, page, size)

        return ResponseEntity.ok(userPage.map { this.userMapper.map(it) })
    }

    @GetMapping(Route.FIND_USER_ADMIN)
    @ApiOperation(value = Constants.GET_MSG + Constants.USERS + Constants.BY_ID_MSG)
    fun getUser(@PathVariable("id") userId: Long): ResponseEntity<Any> {
        val user = this.userService.find(userId).orElseThrow { ExceptionUtil.getNotFound(Constants.USERS, userId) }
        return ResponseEntity.ok(this.userMapper.map(user))
    }


    @PostMapping(Route.DISABLE_USER_ADMIN)
    @ApiOperation(value = Constants.DISABLE_MSG + Constants.USERS + Constants.BY_ID_MSG)
    fun disableUser(
        @PathVariable("id") id: Long,
        @RequestParam("enabled") enabled: Boolean
    ): ResponseEntity<Any> {
        var user = this.userService.find(id).orElseThrow { ExceptionUtil.getNotFound(Constants.USERS, id) }
        user.isEnabled = enabled
        user = this.userService.save(user)
        this.tokenService.revokeAuthentication(user)
        return ResponseEntity.ok(this.userMapper.map(user))
    }

    @PutMapping(Route.CHANGE_USER_ADMIN_ROLE)
    @ApiOperation(value = Constants.CHANGE_ROLE_MSG + Constants.USERS + " " + Constants.ROLES)
    fun changeRole(
        @PathVariable("id") id: Long,
        @RequestParam("roles") roles: List<Long>
    ): ResponseEntity<*> {
        val user = this.userService.setRoles(id, roles)
        return ResponseEntity.ok(this.userMapper.map(user))
    }

    @PatchMapping(Route.CHANGE_USER_ADMIN_PASSWORD)
    @ApiOperation(value = Constants.CHANGE_ROLE_MSG + Constants.USERS + " " + Constants.PASSWORD)
    fun changePassword(
        @PathVariable("id") userId: Long,
        @RequestParam("newPassword") newPassword: String
    ): ResponseEntity<Any> {
        this.userService.setPassword(userId, newPassword)
        return ResponseEntity.ok().build()
    }
}
