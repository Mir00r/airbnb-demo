package com.airbnb.authenticator.domains.roles.controllers

import com.airbnb.authenticator.common.controllers.BaseCrudController
import com.airbnb.authenticator.domains.roles.models.dtos.RoleDto
import com.airbnb.authenticator.domains.roles.models.entities.Role
import com.airbnb.authenticator.domains.roles.models.mappers.RoleMapper
import com.airbnb.authenticator.domains.roles.services.RoleService
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
@Api(tags = [Constants.ROLES_ADMIN], description = Constants.ROLES_ADMIN_API_DETAILS)
class RoleController @Autowired constructor(
    private val roleService: RoleService,
    private val roleMapper: RoleMapper
) : BaseCrudController<RoleDto> {

    @GetMapping(Route.SEARCH_ADMIN_ROLES)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.ROLES_ADMIN)
    override fun search(
        @RequestParam("query", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<RoleDto>> {
        val roles = this.roleService.search(query, page, size)
        return ResponseEntity.ok(roles.map { this.roleMapper.map(it) })
    }

    @GetMapping(Route.FIND_ADMIN_ROLES)
    @ApiOperation(value = Constants.GET_MSG + Constants.ROLES_ADMIN + Constants.BY_ID_MSG)
    override fun find(id: Long): ResponseEntity<RoleDto> {
        val exRole = this.roleService.find(id).orElseThrow { ExceptionUtil.notFound(Role::class.java, id) }
        return ResponseEntity.ok(this.roleMapper.map(exRole))
    }

    @PostMapping(Route.CREATE_ADMIN_ROLES)
    @ApiOperation(value = Constants.POST_MSG + Constants.ROLES_ADMIN)
    override fun create(@Valid @RequestBody dto: RoleDto): ResponseEntity<RoleDto> {

        val role = this.roleService.save(this.roleMapper.map(dto, null))
        return ResponseEntity.ok(this.roleMapper.map(role))
    }

    @PatchMapping(Route.UPDATE_ADMIN_ROLES)
    @ApiOperation(value = Constants.PATCH_MSG + Constants.ROLES_ADMIN)
    override fun update(@PathVariable id: Long, @Valid @RequestBody dto: RoleDto): ResponseEntity<RoleDto> {
        val exRole = this.roleService.find(id).orElseThrow { ExceptionUtil.notFound(Role::class.java, id) }
        val role = this.roleService.save(this.roleMapper.map(dto, exRole))
        return ResponseEntity.ok(this.roleMapper.map(role))
    }

    @DeleteMapping(Route.DELETE_ADMIN_ROLES)
    @ApiOperation(value = Constants.DELETE_MSG + Constants.ROLES_ADMIN)
    override fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        this.roleService.delete(id, true)
        return ResponseEntity.ok().build();
    }
}
