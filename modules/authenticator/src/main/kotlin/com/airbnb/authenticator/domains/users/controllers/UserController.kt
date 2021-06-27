package com.airbnb.authenticator.domains.users.controllers

import com.airbnb.authenticator.common.controllers.BaseCrudController
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
 * @author mir00r on 22/6/21
 */
@RestController
@Api(tags = [Constants.USERS], description = Constants.USERS_API_DETAILS)
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userMapper: UserMapper
) : BaseCrudController<UserDto> {

    @GetMapping(Route.SEARCH_USER)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.USERS)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<UserDto>> {

        val entities = this.userService.search(query, page, size)
        return ResponseEntity.ok(entities.map { this.userMapper.map(it) })
    }

    @GetMapping(Route.FIND_USER)
    @ApiOperation(value = Constants.GET_MSG + Constants.USERS + Constants.BY_ID_MSG)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<UserDto> {
        val user = this.userService.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }
        return ResponseEntity.ok(this.userMapper.map(user))
    }

    @PostMapping(Route.CREATE_USER)
    @ApiOperation(value = Constants.POST_MSG + Constants.USERS)
    override fun create(@Valid @RequestBody dto: UserDto): ResponseEntity<UserDto> {
        val entity = this.userService.save(this.userMapper.map(dto, null))
        return ResponseEntity.ok(this.userMapper.map(entity))
    }

    @PatchMapping(Route.UPDATE_USER)
    @ApiOperation(value = Constants.PATCH_MSG + Constants.USERS)
    override fun update(@PathVariable("id") id: Long, @Valid @RequestBody dto: UserDto): ResponseEntity<UserDto> {
        var entity =
            this.userService.find(id).orElseThrow { ExceptionUtil.notFound(User::class.java, id) }
        entity = this.userService.save(this.userMapper.map(dto, entity))
        return ResponseEntity.ok(this.userMapper.map(entity))
    }

    override fun delete(id: Long): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }
}
