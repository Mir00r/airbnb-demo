package com.airbnb.authenticator.domains.privilege.controllers

import com.airbnb.authenticator.common.controllers.BaseCrudController
import com.airbnb.authenticator.domains.privilege.models.dtos.PrivilegeDto
import com.airbnb.authenticator.domains.privilege.models.entities.Privilege
import com.airbnb.authenticator.domains.privilege.models.mappers.PrivilegeMapper
import com.airbnb.authenticator.domains.privilege.services.PrivilegeService
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
 * @author mir00r on 21/6/21
 */
@RestController
@Api(tags = [Constants.PRIVILEGES_ADMIN], description = Constants.PRIVILEGES_ADMIN_API_DETAILS)
class PrivilegeController @Autowired constructor(
    private val privilegeService: PrivilegeService,
    private val privilegeMapper: PrivilegeMapper
) : BaseCrudController<PrivilegeDto> {

    @GetMapping(Route.SEARCH_ADMIN_PRIVILEGES)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.PRIVILEGES_ADMIN)
    override fun search(
        @RequestParam("query", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<PrivilegeDto>> {

        val privileges = this.privilegeService.search(query, page, size)
        return ResponseEntity.ok(privileges.map { privilege -> this.privilegeMapper.map(privilege) })
    }

    @GetMapping(Route.FIND_ADMIN_PRIVILEGES)
    override fun find(id: Long): ResponseEntity<PrivilegeDto> {
        val entity = this.privilegeService.find(id).orElseThrow { ExceptionUtil.notFound(Privilege::class.java, id) }
        return ResponseEntity.ok(this.privilegeMapper.map(entity))
    }

    @PostMapping(Route.CREATE_ADMIN_PRIVILEGES)
    @ApiOperation(value = Constants.POST_MSG + Constants.PRIVILEGES_ADMIN)
    override fun create(@Valid @RequestBody dto: PrivilegeDto): ResponseEntity<PrivilegeDto> {
        val privilege = this.privilegeService.save(this.privilegeMapper.map(dto, null))
        return ResponseEntity.ok(this.privilegeMapper.map(privilege))
    }

    @PatchMapping(Route.UPDATE_ADMIN_PRIVILEGES)
    @ApiOperation(value = Constants.PATCH_MSG + Constants.PRIVILEGES_ADMIN)
    override fun update(@PathVariable id: Long, @Valid @RequestBody dto: PrivilegeDto): ResponseEntity<PrivilegeDto> {

        var privilege =
            this.privilegeService.find(id).orElseThrow { ExceptionUtil.notFound(Privilege::class.java, id) }
        privilege = this.privilegeService.save(this.privilegeMapper.map(dto, privilege))
        return ResponseEntity.ok(this.privilegeMapper.map(privilege))
    }

    override fun delete(id: Long): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }
}
