package com.airbnb.rentalprocessor.domains.rentalrequests.services

import com.airbnb.authenticator.common.services.BaseCrudService
import com.airbnb.rentalprocessor.domains.rentalrequests.models.entities.RentalRequest
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import org.springframework.data.domain.Page
import java.time.Instant

interface RentalRequestService : BaseCrudService<RentalRequest> {
    fun changeStatus(id: Long, statuses: RequestStatuses): RentalRequest
    fun cancel(id: Long): RentalRequest

    fun advanceSearch(
        query: String, page: Int, size: Int,
        status: RequestStatuses?,
        requestedById: Long?,
        requestedToId: Long?,
        cancelledById: Long?,
        assignedToId: Long?,
        householdId: Long?,
        checkIn: Instant?,
        checkOut: Instant?
    ): Page<RentalRequest>
}
