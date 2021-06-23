package com.airbnb.rentalprocessor.domains.households.services

import com.airbnb.authenticator.common.services.BaseCrudService
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.enums.PropertyTypes
import com.airbnb.rentalprocessor.domains.households.models.enums.RentTypes
import org.springframework.data.domain.Page
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestParam

interface HouseholdService : BaseCrudService<Household> {
    fun search(
        query: String, page: Int, size: Int,
        propertyType: PropertyTypes?,
        rentType: RentTypes?,
        status: HouseholdStatuses?,
        available: Boolean?,
        bed: Byte?,
        bath: Byte?,
        balcony: Byte?,
        householdSize: Long?,
        price: Double?,
        latitude: Double?,
        longitude: Double?,
        altitude: Double?
    ): Page<Household>

    fun changeStatus(id: Long, status: HouseholdStatuses): Household
}
