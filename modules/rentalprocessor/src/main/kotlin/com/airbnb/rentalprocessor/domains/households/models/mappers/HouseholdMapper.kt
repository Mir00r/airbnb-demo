package com.airbnb.rentalprocessor.domains.households.models.mappers

import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.households.models.dtos.AmenitiesDto
import com.airbnb.rentalprocessor.domains.households.models.dtos.HouseholdAddressDto
import com.airbnb.rentalprocessor.domains.households.models.dtos.HouseholdDto
import com.airbnb.rentalprocessor.domains.households.models.entities.Amenities
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.models.entities.HouseholdAddress
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class HouseholdMapper : BaseMapper<Household, HouseholdDto> {

    override fun map(entity: Household): HouseholdDto {
        val dto = HouseholdDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.availableFrom = entity.availableFrom
            this.numberOfBalcony = entity.numberOfBalcony
            this.numberOfBath = entity.numberOfBath
            this.numberOfBed = entity.numberOfBed
            this.propertyType = entity.propertyType
            this.rentType = entity.rentType
            this.rentPrice = entity.rentPrice
            this.size = entity.size
            this.hostName = entity.hostName
            this.available = entity.available
            this.amenities = mapAmenities(entity.amenities ?: Amenities())
            this.address = mapAddress(entity.address ?: HouseholdAddress())
            this.images = entity.images
            this.status = entity.status
        }

        return dto
    }

    override fun map(dto: HouseholdDto, exEntity: Household?): Household {
        val entity = exEntity ?: Household()

        entity.apply {
            this.status = HouseholdStatuses.PENDING
            this.availableFrom = if (dto.availableFrom != null && dto.availableFrom!!.isBefore(Instant.now())) {
                throw ExceptionUtil.invalid("Available date can not less then current date")
            } else dto.availableFrom
            this.available = false
            this.numberOfBalcony = dto.numberOfBalcony
            this.numberOfBath = dto.numberOfBath
            this.numberOfBed = dto.numberOfBed
            this.propertyType = dto.propertyType
            this.rentType = dto.rentType
            this.rentPrice = dto.rentPrice
            this.size = dto.size
            this.hostName = dto.hostName ?: SecurityContext.getLoggedInUsername()
            this.available = dto.available
            this.amenities = mapAmenities(dto.amenities ?: AmenitiesDto())
            this.address = mapAddress(dto.address ?: HouseholdAddressDto())
            this.images = dto.images
        }
        return entity
    }

    private fun mapAmenities(entity: Amenities): AmenitiesDto {
        val dto = AmenitiesDto()
        dto.apply {
            this.hasBBQArea = entity.hasBBQArea
            this.hasCCTV = entity.hasCCTV
            this.hasDining = entity.hasDining
            this.hasDrawing = entity.hasDrawing
            this.hasElectricity = entity.hasElectricity
            this.hasElevator = entity.hasElevator
            this.hasFurnished = entity.hasFurnished
            this.hasGas = entity.hasGas
            this.hasGym = entity.hasGym
            this.hasHairDryer = entity.hasHairDryer
            this.hastTiled = entity.hastTiled
            this.hasMosque = entity.hasMosque
            this.hasParking = entity.hasParking
            this.hasKitchen = entity.hasKitchen
            this.hasRoofAccess = entity.hasRoofAccess
            this.hasSwimmingPool = entity.hasSwimmingPool
            this.hasWater = entity.hasWater
        }
        return dto
    }

    private fun mapAmenities(dto: AmenitiesDto): Amenities {
        val entity = Amenities()
        entity.apply {
            this.hasBBQArea = dto.hasBBQArea
            this.hasCCTV = dto.hasCCTV
            this.hasDining = dto.hasDining
            this.hasDrawing = dto.hasDrawing
            this.hasElectricity = dto.hasElectricity
            this.hasElevator = dto.hasElevator
            this.hasFurnished = dto.hasFurnished
            this.hasGas = dto.hasGas
            this.hasGym = dto.hasGym
            this.hasHairDryer = dto.hasHairDryer
            this.hastTiled = dto.hastTiled
            this.hasMosque = dto.hasMosque
            this.hasParking = dto.hasParking
            this.hasKitchen = dto.hasKitchen
            this.hasRoofAccess = dto.hasRoofAccess
            this.hasSwimmingPool = dto.hasSwimmingPool
            this.hasWater = dto.hasWater
        }
        return entity
    }

    private fun mapAddress(entity: HouseholdAddress): HouseholdAddressDto {
        val dto = HouseholdAddressDto()
        dto.apply {
            this.lineOne = entity.lineOne
            this.lineTwo = entity.lineTwo
            this.country = entity.country
            this.city = entity.city
            this.state = entity.state
            this.zipcode = entity.zipcode
            this.floorNumber = entity.floorNumber
            this.latitude = entity.latitude
            this.longitude = entity.longitude
            this.altitude = entity.altitude
        }
        return dto
    }

    private fun mapAddress(dto: HouseholdAddressDto): HouseholdAddress {
        val entity = HouseholdAddress()
        entity.apply {
            this.lineOne = dto.lineOne
            this.lineTwo = dto.lineTwo
            this.country = dto.country
            this.city = dto.city
            this.state = dto.state
            this.zipcode = dto.zipcode
            this.floorNumber = dto.floorNumber
            this.latitude = dto.latitude
            this.longitude = dto.longitude
            this.altitude = dto.altitude
        }
        return entity
    }
}
