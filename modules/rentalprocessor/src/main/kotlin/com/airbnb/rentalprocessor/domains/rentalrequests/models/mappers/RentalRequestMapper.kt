package com.airbnb.rentalprocessor.domains.rentalrequests.models.mappers

import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.authenticator.domains.users.services.UserService
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.services.HouseholdService
import com.airbnb.rentalprocessor.domains.rentalrequests.models.dtos.RentalRequestDto
import com.airbnb.rentalprocessor.domains.rentalrequests.models.entities.RentalRequest
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RentalRequestMapper @Autowired constructor(
    private val userService: UserService,
    private val householdService: HouseholdService
) : BaseMapper<RentalRequest, RentalRequestDto> {

    override fun map(entity: RentalRequest): RentalRequestDto {
        val dto = RentalRequestDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.assignedTo = entity.assignedTo?.id
            this.cancelledBy = entity.cancelledBy?.id
            this.requestedBy = entity.requestedBy.id ?: 0
            this.requestedTo = entity.requestedTo.id ?: 0
            this.note = entity.note
            this.checkIn = entity.checkIn
            this.checkOut = entity.checkOut
            this.status = entity.status
            this.householdId = entity.household.id ?: 0
        }
        return dto
    }

    override fun map(dto: RentalRequestDto, exEntity: RentalRequest?): RentalRequest {
        val entity = exEntity ?: RentalRequest()

        entity.apply {
            this.household = householdService.find(dto.householdId)
                .orElseThrow { ExceptionUtil.notFound(Household::class.java, dto.householdId) }
            this.requestedBy = userService.find(dto.requestedBy)
                .orElseThrow { ExceptionUtil.notFound(User::class.java, dto.requestedBy) }
            this.requestedTo = userService.find(dto.requestedTo)
                .orElseThrow { ExceptionUtil.notFound(User::class.java, dto.requestedTo) }
            this.assignedTo =
                dto.assignedTo?.let {
                    userService.find(it).orElseThrow { ExceptionUtil.notFound(User::class.java, it) }
                }
            this.cancelledBy =
                dto.cancelledBy?.let {
                    userService.find(it).orElseThrow { ExceptionUtil.notFound(User::class.java, it) }
                }
            this.note = dto.note
            this.checkIn = dto.checkIn
            this.checkOut = dto.checkOut
            this.status = RequestStatuses.PENDING
        }

        return entity
    }
}
