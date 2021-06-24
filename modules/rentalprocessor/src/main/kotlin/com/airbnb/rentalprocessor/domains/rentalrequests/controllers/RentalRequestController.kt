package com.airbnb.rentalprocessor.domains.rentalrequests.controllers

import com.airbnb.authenticator.common.controllers.BaseCrudController
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.rentalrequests.models.dtos.RentalRequestDto
import com.airbnb.rentalprocessor.domains.rentalrequests.models.entities.RentalRequest
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import com.airbnb.rentalprocessor.domains.rentalrequests.models.mappers.RentalRequestMapper
import com.airbnb.rentalprocessor.domains.rentalrequests.services.RentalRequestService
import com.airbnb.rentalprocessor.routing.Route
import com.airbnb.rentalprocessor.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import javax.validation.Valid

@RestController
@Api(tags = [Constants.RENTAL_REQUEST], description = Constants.REST_API)
class RentalRequestController @Autowired constructor(
    private val rentalRequestService: RentalRequestService,
    private val rentalRequestMapper: RentalRequestMapper
) : BaseCrudController<RentalRequestDto> {

    @GetMapping(Route.V1.ADVANCE_SEARCH_RENTAL_REQUESTS)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.RENTAL_REQUEST)
    fun advanceSearch(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("status", required = false) status: RequestStatuses?,
        @RequestParam("requested_by_id", required = false) requestedById: Long?,
        @RequestParam("requested_to_id", required = false) requestedToId: Long?,
        @RequestParam("cancelled_by_id", required = false) cancelledById: Long?,
        @RequestParam("assigned_to_id", required = false) assignedToId: Long?,
        @RequestParam("household_id", required = false) householdId: Long?,
        @RequestParam("check_in", required = false) checkIn: Instant?,
        @RequestParam("check_out", required = false) checkOut: Instant?
    ): ResponseEntity<Page<RentalRequestDto>> {
        val entities = this.rentalRequestService.advanceSearch(
            query,
            page,
            size,
            status,
            requestedById,
            requestedToId,
            cancelledById,
            assignedToId,
            householdId,
            checkIn,
            checkOut
        )
        return ResponseEntity.ok(entities.map { this.rentalRequestMapper.map(it) })
    }

    @GetMapping(Route.V1.SEARCH_RENTAL_REQUESTS)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.RENTAL_REQUEST)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<RentalRequestDto>> {
        val entities = this.rentalRequestService.search(query, page, size)
        return ResponseEntity.ok(entities.map { this.rentalRequestMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_RENTAL_REQUEST)
    @ApiOperation(value = "Find rental request by id")
    override fun find(@PathVariable("id") id: Long): ResponseEntity<RentalRequestDto> {
        val entity =
            this.rentalRequestService.find(id).orElseThrow { ExceptionUtil.notFound(RentalRequest::class.java, id) }
        return ResponseEntity.ok(this.rentalRequestMapper.map(entity))
    }

    @GetMapping(Route.V1.ADMIN_RENTAL_REQUESTS_CHANGE_STATUS)
    @ApiOperation(value = "Change status of rental request through id by admin")
    fun changeStatusByAdmin(
        @PathVariable("id") id: Long,
        @RequestParam("status") status: RequestStatuses
    ): ResponseEntity<RentalRequestDto> {
        return ResponseEntity.ok(this.rentalRequestMapper.map(this.rentalRequestService.changeStatus(id, status)))
    }

    @GetMapping(Route.V1.CANCEL_RENTAL_REQUESTS)
    @ApiOperation(value = "Cancel rental request through id by user")
    fun cancelRequest(
        @PathVariable("id") id: Long
    ): ResponseEntity<RentalRequestDto> {
        return ResponseEntity.ok(this.rentalRequestMapper.map(this.rentalRequestService.cancel(id)))
    }

    @PostMapping(Route.V1.CREATE_RENTAL_REQUEST)
    @ApiOperation(value = Constants.POST_MSG + Constants.RENTAL_REQUEST)
    override fun create(@Valid @RequestBody dto: RentalRequestDto): ResponseEntity<RentalRequestDto> {
        val entity = this.rentalRequestService.save(this.rentalRequestMapper.map(dto, null))
        return ResponseEntity.ok(this.rentalRequestMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_RENTAL_REQUEST)
    @ApiOperation(value = Constants.PATCH_MSG + Constants.RENTAL_REQUEST)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody dto: RentalRequestDto
    ): ResponseEntity<RentalRequestDto> {
        var entity =
            this.rentalRequestService.find(id).orElseThrow { ExceptionUtil.notFound(RentalRequest::class.java, id) }
        entity = this.rentalRequestService.save(this.rentalRequestMapper.map(dto, entity))
        return ResponseEntity.ok(this.rentalRequestMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_RENTAL_REQUEST)
    @ApiOperation(value = Constants.DELETE_MSG + Constants.RENTAL_REQUEST)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.rentalRequestService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
