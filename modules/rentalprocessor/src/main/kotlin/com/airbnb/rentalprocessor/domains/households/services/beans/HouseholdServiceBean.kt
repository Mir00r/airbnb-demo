package com.airbnb.rentalprocessor.domains.households.services.beans

import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.utils.Validation
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
import java.util.*

@Service
class HouseholdServiceBean @Autowired constructor(
    private val householdRepository: HouseholdRepository
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

    override fun search(query: String, page: Int, size: Int): Page<Household> {
        return this.householdRepository.search(
            query,
            SecurityContext.getLoggedInUsername(),
            PageAttr.getPageRequest(page, size)
        )
    }

    override fun changeStatus(id: Long, status: HouseholdStatuses): Household {
        val entity = this.find(id).orElseThrow { ExceptionUtil.notFound(Household::class.java, id) }
        if (entity.status == HouseholdStatuses.ACCEPTED) throw ExceptionUtil.exists("Already accepted of this household id: $id")
        entity.status = status
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
