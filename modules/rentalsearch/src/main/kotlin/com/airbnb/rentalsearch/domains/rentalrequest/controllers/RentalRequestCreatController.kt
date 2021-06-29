package com.airbnb.rentalsearch.domains.rentalrequest.controllers

import com.airbnb.rentalprocessor.domains.rentalrequests.models.dtos.RentalRequestDto
import com.airbnb.rentalprocessor.domains.rentalrequests.models.mappers.RentalRequestMapper
import com.airbnb.rentalprocessor.domains.rentalrequests.services.RentalRequestService
import com.airbnb.rentalsearch.routing.Route
import com.airbnb.rentalsearch.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @project IntelliJ IDEA
 * @author mir00r on 25/6/21
 */
@RestController
@Api(tags = [Constants.RENTAL_SEARCH], description = Constants.REST_API)
class RentalRequestCreatController @Autowired constructor(
    private val rentalRequestService: RentalRequestService,
    private val rentalRequestMapper: RentalRequestMapper
) {

    @PostMapping(Route.V1.CREAT_RENTAL_REQUEST)
    @ApiOperation(value = "Create new rental request from searched household")
    fun create(@Valid @RequestBody dto: RentalRequestDto): ResponseEntity<RentalRequestDto> {
        val entity = this.rentalRequestService.save(this.rentalRequestMapper.map(dto, null))
        return ResponseEntity.ok(this.rentalRequestMapper.map(entity))
    }
}
