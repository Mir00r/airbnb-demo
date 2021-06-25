package com.airbnb.rentalsearch.domains.household.controllers

import com.airbnb.rentalprocessor.domains.households.models.dtos.HouseholdDto
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.mappers.HouseholdMapper
import com.airbnb.rentalprocessor.domains.households.services.HouseholdService
import com.airbnb.rentalsearch.routing.Route
import com.airbnb.rentalsearch.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

/**
 * @project IntelliJ IDEA
 * @author mir00r on 25/6/21
 */
@RestController
@Api(tags = [Constants.HOUSEHOLDS_SEARCH], description = Constants.REST_API)
class HouseholdSearchController @Autowired constructor(
    private val householdService: HouseholdService,
    private val householdMapper: HouseholdMapper
) {

    @GetMapping(Route.V1.SEARCH_HOUSEHOLDS)
    @ApiOperation(value = "Search household using addresses, price range and date range also latitude, longitude and altitude")
    fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("start_price", required = false) startPrice: Double?,
        @RequestParam("end_price", required = false) endPrice: Double?,
        @RequestParam("from_date", required = false) fromDate: Instant?,
        @RequestParam("to_date", required = false) toDate: Instant?,
        @RequestParam("latitude", required = false) latitude: Double?,
        @RequestParam("longitude", required = false) longitude: Double?,
        @RequestParam("altitude", required = false) altitude: Double?
    ): ResponseEntity<Page<HouseholdDto>> {

        val entities = this.householdService.search(
            query, page, size, HouseholdStatuses.ACCEPTED, startPrice, endPrice, fromDate, toDate,
            latitude, longitude, altitude
        )
        return ResponseEntity.ok(entities.map { this.householdMapper.map(it) })
    }
}
