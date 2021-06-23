package com.airbnb.rentalprocessor.domains.households.controllers

import com.airbnb.authenticator.common.controllers.BaseCrudController
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.households.models.dtos.HouseholdDto
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.enums.PropertyTypes
import com.airbnb.rentalprocessor.domains.households.models.enums.RentTypes
import com.airbnb.rentalprocessor.domains.households.models.mappers.HouseholdMapper
import com.airbnb.rentalprocessor.domains.households.services.HouseholdService
import com.airbnb.rentalprocessor.routing.Route
import com.airbnb.rentalprocessor.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(tags = [Constants.HOUSEHOLDS], description = "Rest API")
class HouseholdController @Autowired constructor(
    private val householdService: HouseholdService,
    private val householdMapper: HouseholdMapper
) : BaseCrudController<HouseholdDto> {

    @GetMapping(Route.V1.ADVANCE_SEARCH_HOUSEHOLDS)
    @ApiOperation(value = "Advance search option for household")
    fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("property_type", required = false) propertyType: PropertyTypes?,
        @RequestParam("rentType", required = false) rentType: RentTypes?,
        @RequestParam("status", required = false) status: HouseholdStatuses?,
        @RequestParam("available", required = false) available: Boolean?,
        @RequestParam("bed", required = false) bed: Byte?,
        @RequestParam("bath", required = false) bath: Byte?,
        @RequestParam("balcony", required = false) balcony: Byte?,
        @RequestParam("household_size", required = false) householdSize: Long?,
        @RequestParam("price", required = false) price: Double?,
    ): ResponseEntity<Page<HouseholdDto>> {

        val entities = this.householdService.search(
            query,
            page,
            size,
            propertyType,
            rentType,
            status,
            available,
            bed,
            bath,
            balcony,
            householdSize,
            price
        )
        return ResponseEntity.ok(entities.map { this.householdMapper.map(it) })
    }

    @GetMapping(Route.V1.SEARCH_HOUSEHOLDS)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.HOUSEHOLDS)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<HouseholdDto>> {

        val entities = this.householdService.search(query, page, size)
        return ResponseEntity.ok(entities.map { this.householdMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_HOUSEHOLD)
    @ApiOperation(value = "Find Household by id")
    override fun find(@PathVariable("id") id: Long): ResponseEntity<HouseholdDto> {
        val entity = this.householdService.find(id).orElseThrow { ExceptionUtil.notFound(Household::class.java, id) }
        return ResponseEntity.ok(this.householdMapper.map(entity))
    }

    @PatchMapping(Route.V1.CHANGE_HOUSEHOLDS_STATUS)
    @ApiOperation(value = "Change household status")
    fun changeStatus(
        @PathVariable("id") id: Long,
        @RequestParam("status") status: HouseholdStatuses
    ): ResponseEntity<HouseholdDto> {
        return ResponseEntity.ok(this.householdMapper.map(this.householdService.changeStatus(id, status)))
    }

    @PostMapping(Route.V1.CREATE_HOUSEHOLD)
    @ApiOperation(value = Constants.POST_MSG + Constants.HOUSEHOLDS)
    override fun create(@Valid @RequestBody dto: HouseholdDto): ResponseEntity<HouseholdDto> {
        val entity = this.householdService.save(this.householdMapper.map(dto, null))
        return ResponseEntity.ok(this.householdMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_HOUSEHOLD)
    @ApiOperation(value = Constants.PATCH_MSG + Constants.HOUSEHOLDS)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody dto: HouseholdDto
    ): ResponseEntity<HouseholdDto> {
        var entity = this.householdService.find(id).orElseThrow { ExceptionUtil.notFound(Household::class.java, id) }
        entity = this.householdService.save(this.householdMapper.map(dto, entity))
        return ResponseEntity.ok(this.householdMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_HOUSEHOLD)
    @ApiOperation(value = Constants.DELETE_MSG + Constants.HOUSEHOLDS)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.householdService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
