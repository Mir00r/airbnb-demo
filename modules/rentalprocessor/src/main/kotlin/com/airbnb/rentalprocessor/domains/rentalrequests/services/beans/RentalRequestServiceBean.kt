package com.airbnb.rentalprocessor.domains.rentalrequests.services.beans

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.utils.Validation
import com.airbnb.common.services.MailService
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.common.utils.PageAttr
import com.airbnb.rentalprocessor.domains.households.repositories.HouseholdRepository
import com.airbnb.rentalprocessor.domains.rentalrequests.models.entities.RentalRequest
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import com.airbnb.rentalprocessor.domains.rentalrequests.repositories.RentalRequestRepository
import com.airbnb.rentalprocessor.domains.rentalrequests.services.RentalRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Service
open class RentalRequestServiceBean @Autowired constructor(
    private val rentalRequestRepository: RentalRequestRepository,
    private val householdRepository: HouseholdRepository,
    private val mailService: MailService
) : RentalRequestService {

    @Transactional
    override fun changeStatus(id: Long, statuses: RequestStatuses): RentalRequest {
        var entity =
            this.rentalRequestRepository.find(id).orElseThrow { ExceptionUtil.notFound(RentalRequest::class.java, id) }
        entity.status = statuses
        entity = this.save(entity)
        if (entity.status == RequestStatuses.PENDING || entity.status == RequestStatuses.CONFIRMED) {
            entity.household.available = false
            entity.household.availableFrom = entity.checkOut
        } else {
            entity.household.available = true
        }
        this.householdRepository.save(entity.household)
        this.mailService.sendEmail(
            entity.requestedTo.email,
            "Rental request update",
            "Hello here is your latest update of rental request status is: ${entity.status}"
        )
        return entity
    }

    @Transactional
    override fun cancel(id: Long): RentalRequest {
        var entity =
            this.rentalRequestRepository.find(id).orElseThrow { ExceptionUtil.notFound(RentalRequest::class.java, id) }
        val cancelDate: Instant = entity.checkIn.minus(7, ChronoUnit.DAYS)
        if (Instant.now() > cancelDate) throw ExceptionUtil.invalid("Cancelling should be done at least 7 days before the start of the Check In date.")

        Validation.isAccessResource(entity.createdBy ?: "")

        entity.status = RequestStatuses.CANCELED
        entity.household.available = true
        entity.household.availableFrom = Instant.now()
        entity = this.save(entity)
        this.householdRepository.save(entity.household)
        this.mailService.sendEmail(
            entity.requestedTo.email,
            "Rental request update",
            "Hello rental request cancel successfully"
        )
        return entity
    }

    override fun advanceSearch(
        query: String, page: Int,
        size: Int, createdBy: String?,
        status: RequestStatuses?,
        requestedById: Long?,
        requestedToId: Long?,
        cancelledById: Long?,
        assignedToId: Long?,
        householdId: Long?,
        checkIn: Instant?,
        checkOut: Instant?
    ): Page<RentalRequest> {
        val created = createdBy ?: SecurityContext.getLoggedInUsername()
        if (checkIn != null && checkOut != null)
            return this.rentalRequestRepository.search(
                query, PageAttr.getPageRequest(page, size), created,
                status,
                requestedById,
                requestedToId,
                cancelledById,
                assignedToId,
                householdId,
                checkIn,
                checkOut
            ) else if (checkIn != null && checkOut == null) {
            return this.rentalRequestRepository.searchIn(
                query, PageAttr.getPageRequest(page, size), createdBy,
                status,
                requestedById,
                requestedToId,
                cancelledById,
                assignedToId,
                householdId,
                checkIn
            )
        } else if (checkIn == null && checkOut != null) {
            return this.rentalRequestRepository.searchOut(
                query, PageAttr.getPageRequest(page, size), createdBy,
                status,
                requestedById,
                requestedToId,
                cancelledById,
                assignedToId,
                householdId,
                checkOut
            )
        } else return this.rentalRequestRepository.search(
            query, PageAttr.getPageRequest(page, size), createdBy,
            status,
            requestedById,
            requestedToId,
            cancelledById,
            assignedToId,
            householdId
        )
    }

    override fun search(query: String, page: Int, size: Int): Page<RentalRequest> {
        return this.rentalRequestRepository.search(
            query,
            SecurityContext.getLoggedInUsername(),
            PageAttr.getPageRequest(page, size)
        )
    }

    override fun save(entity: RentalRequest): RentalRequest {
        this.validate(entity)
        return this.rentalRequestRepository.save(entity)
    }

    override fun find(id: Long): Optional<RentalRequest> {
        return this.rentalRequestRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound(RentalRequest::class.java, id) }
            Validation.isAccessResource(entity.createdBy ?: "")
            entity.deleted = true
            this.rentalRequestRepository.save(entity)
        }
        this.rentalRequestRepository.deleteById(id)
    }

    override fun validate(entity: RentalRequest) {
        if (entity.isNew()) {
            if (entity.status == RequestStatuses.PENDING && entity.cancelledBy != null) throw ExceptionUtil.invalid("Cancelled by should be empty while create new rental request !")
        }
        if (entity.checkIn.isAfter(entity.checkOut)) throw ExceptionUtil.invalid("Check in date: ${entity.checkIn} should be less then check out: ${entity.checkOut} !")
        if (!entity.household.available) throw ExceptionUtil.invalid("Currently this household: ${entity.household.id} is not available for rental request !")
    }
}
