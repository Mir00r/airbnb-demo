package com.airbnb.rentalprocessor.domains.households.repositories

import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.models.enums.HouseholdStatuses
import com.airbnb.rentalprocessor.domains.households.models.enums.PropertyTypes
import com.airbnb.rentalprocessor.domains.households.models.enums.RentTypes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Repository
interface HouseholdRepository : JpaRepository<Household, Long> {

    @Query("SELECT hh FROM Household hh WHERE (:q IS NULL OR LOWER(hh.createdBy) LIKE %:q%) AND hh.deleted=FALSE")
    fun search(@Param("q") query: String?, pageable: Pageable): Page<Household>

    @Query("SELECT hh FROM Household hh WHERE (:q IS NULL OR LOWER(hh.createdBy) LIKE %:q%) AND (:createdBy IS NULL OR hh.createdBy=:createdBy) AND hh.deleted=FALSE")
    fun search(@Param("q") query: String?, @Param("createdBy") createdBy: String?, pageable: Pageable): Page<Household>

    @Query("SELECT hh FROM Household hh WHERE (:q IS NULL OR LOWER(hh.createdBy) LIKE %:q% OR LOWER(hh.hostName) LIKE %:q% OR LOWER(hh.address.country) LIKE %:q% OR LOWER(hh.address.city) LIKE %:q% OR LOWER(hh.address.state) LIKE %:q% OR LOWER(hh.address.zipcode) LIKE %:q% ) AND (:createdBy IS NULL OR hh.createdBy=:createdBy) AND (:propertyType IS NULL OR hh.propertyType=:propertyType) AND (:rentType IS NULL OR hh.rentType=:rentType) AND (:status IS NULL OR hh.status=:status) AND (:available IS NULL OR hh.available=:available) AND (:bed IS NULL OR hh.numberOfBed=:bed) AND (:bath IS NULL OR hh.numberOfBath=:bath) AND (:balcony IS NULL OR hh.numberOfBalcony=:balcony) AND (:size IS NULL OR hh.size=:size) AND (:price IS NULL OR hh.rentPrice=:price) AND (:latitude IS NULL OR hh.address.latitude=:latitude) AND (:longitude IS NULL OR hh.address.longitude=:longitude) AND (:altitude IS NULL OR hh.address.altitude=:altitude) AND hh.deleted=FALSE")
    fun search(
        @Param("q") query: String?,
        @Param("createdBy") createdBy: String?,
        @Param("propertyType") propertyType: PropertyTypes?,
        @Param("rentType") rentType: RentTypes?,
        @Param("status") status: HouseholdStatuses?,
        @Param("available") available: Boolean?,
        @Param("bed") bed: Byte?,
        @Param("bath") bath: Byte?,
        @Param("balcony") balcony: Byte?,
        @Param("size") size: Long?,
        @Param("price") price: Double?,
        @Param("latitude") latitude: Double?,
        @Param("longitude") longitude: Double?,
        @Param("altitude") altitude: Double?,
        pageable: Pageable
    ): Page<Household>

    @Query("SELECT hh FROM Household hh WHERE hh.id=:id AND hh.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Household>

}
