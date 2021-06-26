package com.airbnb.rentalprocessor.domains.households.services.beans

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.users.services.UserService
import com.airbnb.authenticator.utils.Validation
import com.airbnb.common.services.MailService
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.common.utils.PageAttr
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.enums.PropertyTypes
import com.airbnb.rentalprocessor.domains.households.models.enums.RentTypes
import com.airbnb.rentalprocessor.domains.households.repositories.HouseholdRepository
import com.airbnb.rentalprocessor.domains.households.services.HouseholdService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class HouseholdServiceBean @Autowired constructor(
    private val householdRepository: HouseholdRepository,
    private val mailService: MailService,
    private val userService: UserService
) : HouseholdService {

    override fun search(
        query: String,
        page: Int,
        size: Int,
        createdBy: String?,
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
    ): Page<Household> {
        return this.householdRepository.search(
            query, createdBy ?: SecurityContext.getLoggedInUsername(),
            propertyType,
            rentType,
            status,
            available,
            bed,
            bath,
            balcony,
            householdSize,
            price,
            latitude, longitude, altitude,
            PageAttr.getPageRequest(page, size)
        )
    }

    override fun search(
        query: String,
        page: Int,
        size: Int,
        status: HouseholdStatuses?,
        startPrice: Double?,
        endPrice: Double?,
        from: Instant?,
        to: Instant?,
        latitude: Double?,
        longitude: Double?,
        altitude: Double?
    ): Page<Household> {
        if (startPrice != null && endPrice != null && from != null && to != null)
            return this.householdRepository.search(
                query, status,
                startPrice,
                endPrice,
                from,
                to,
                longitude,
                longitude,
                altitude,
                PageAttr.getPageRequest(page, size)
            )
        else if (startPrice != null && endPrice != null)
            return this.householdRepository.searchPrice(
                query, status,
                startPrice,
                endPrice,
                longitude,
                longitude,
                altitude,
                PageAttr.getPageRequest(page, size)
            )
        else if (from != null && to != null)
            return this.householdRepository.searchWithAvailableDate(
                query, status,
                from,
                to,
                longitude,
                longitude,
                altitude,
                PageAttr.getPageRequest(page, size)
            )
        else
            return this.householdRepository.searchWithAddress(
                query, status, latitude, longitude, altitude,
                PageAttr.getPageRequest(page, size)
            )
    }

    override fun search(query: String, page: Int, size: Int): Page<Household> {
        return this.householdRepository.search(
            query, SecurityContext.getLoggedInUsername(),
            PageAttr.getPageRequest(page, size)
        )
    }

    override fun changeStatus(id: Long, status: HouseholdStatuses): Household {
        var entity = this.find(id).orElseThrow { ExceptionUtil.notFound(Household::class.java, id) }
        if (entity.status == HouseholdStatuses.ACCEPTED) throw ExceptionUtil.exists("Already accepted of this household id: $id")
        entity.status = status
        entity = this.save(entity)
        val user = this.userService.findByUsername(entity.getLoggedInUsername())
            .orElseThrow { ExceptionUtil.notFound("Household created user not found", entity.createdBy ?: "") }
        this.mailService.sendEmail(
            user.email,
            "Household status change update",
            "Dear ${user.username} your requested household status: ${entity.status} change by admin"
        )
        return entity
    }

    override fun changeAvailableDate(id: Long, availableFrom: Instant): Household {
        val entity = this.find(id).orElseThrow { ExceptionUtil.notFound(Household::class.java, id) }
        if (entity.status == HouseholdStatuses.PENDING || entity.status == HouseholdStatuses.REJECTED) throw ExceptionUtil.exists(
            "The household is in: ${entity.status} state so can not change the available date"
        )
        Validation.isAccessResource(entity.createdBy ?: "")
        if (entity.available) throw ExceptionUtil.invalid("Household is not available right now")
        entity.availableFrom = availableFrom
        entity.available = true
        return this.save(entity)
    }

    override fun save(entity: Household): Household {
        this.validate(entity)
        return this.householdRepository.save(entity)
    }

    override fun find(id: Long): Optional<Household> {
        return this.householdRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound(Household::class.java, id) }
            Validation.isAccessResource(entity.createdBy ?: "")
            entity.deleted = true
            this.householdRepository.save(entity)
        }
        this.householdRepository.deleteById(id)
    }

    override fun validate(entity: Household) {

    }
}
