package com.airbnb.rentalprocessor.domains.rentalrequests.repositories

import com.airbnb.rentalprocessor.domains.rentalrequests.models.entities.RentalRequest
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface RentalRequestRepository : JpaRepository<RentalRequest, Long> {

    @Query("SELECT rr FROM RentalRequest rr WHERE (:q IS NULL OR LOWER(rr.createdBy) LIKE %:q%) AND rr.deleted=FALSE")
    fun search(@Param("q") query: String?, pageable: Pageable): Page<RentalRequest>

    @Query("SELECT rr FROM RentalRequest rr WHERE (:q IS NULL OR LOWER(rr.createdBy) LIKE %:q%) AND (:createdBy IS NULL OR rr.createdBy=:createdBy) AND rr.deleted=FALSE")
    fun search(@Param("q") query: String?, @Param("createdBy") createdBy: String?, pageable: Pageable): Page<RentalRequest>

    @Query("SELECT rr FROM RentalRequest rr WHERE (:q IS NULL OR LOWER(rr.createdBy) LIKE %:q%) AND (:createdBy IS NULL OR rr.createdBy=:createdBy) AND (:status IS NULL OR rr.status=:status) AND (:requestedById IS NULL OR rr.requestedBy.id=:requestedById) AND (:requestedToId IS NULL OR rr.requestedTo.id=:requestedToId) AND (:cancelledById IS NULL OR rr.cancelledBy.id=:cancelledById) AND (:assignedToId IS NULL OR rr.assignedTo.id=:assignedToId) AND (:householdId IS NULL OR rr.household.id=:householdId) AND (rr.checkIn=:checkIn) AND (rr.checkOut=:checkOut) AND rr.deleted=FALSE")
    fun search(
        @Param("q") query: String?, pageable: Pageable,
        @Param("createdBy") createdBy: String?,
        @Param("status") status: RequestStatuses?,
        @Param("requestedById") requestedById: Long?,
        @Param("requestedToId") requestedToId: Long?,
        @Param("cancelledById") cancelledById: Long?,
        @Param("assignedToId") assignedToId: Long?,
        @Param("householdId") householdId: Long?,
        @Param("checkIn") checkIn: Instant,
        @Param("checkOut") checkOut: Instant
    ): Page<RentalRequest>

    @Query("SELECT rr FROM RentalRequest rr WHERE (:q IS NULL OR LOWER(rr.createdBy) LIKE %:q%) AND (:createdBy IS NULL OR rr.createdBy=:createdBy) AND (:status IS NULL OR rr.status=:status) AND (:requestedById IS NULL OR rr.requestedBy.id=:requestedById) AND (:requestedToId IS NULL OR rr.requestedTo.id=:requestedToId) AND (:cancelledById IS NULL OR rr.cancelledBy.id=:cancelledById) AND (:assignedToId IS NULL OR rr.assignedTo.id=:assignedToId) AND (:householdId IS NULL OR rr.household.id=:householdId) AND (rr.checkIn=:checkIn) AND rr.deleted=FALSE")
    fun searchIn(
        @Param("q") query: String?, pageable: Pageable,
        @Param("createdBy") createdBy: String?,
        @Param("status") status: RequestStatuses?,
        @Param("requestedById") requestedById: Long?,
        @Param("requestedToId") requestedToId: Long?,
        @Param("cancelledById") cancelledById: Long?,
        @Param("assignedToId") assignedToId: Long?,
        @Param("householdId") householdId: Long?,
        @Param("checkIn") checkIn: Instant
    ): Page<RentalRequest>

    @Query("SELECT rr FROM RentalRequest rr WHERE (:q IS NULL OR LOWER(rr.createdBy) LIKE %:q%) AND (:createdBy IS NULL OR rr.createdBy=:createdBy) AND (:status IS NULL OR rr.status=:status) AND (:requestedById IS NULL OR rr.requestedBy.id=:requestedById) AND (:requestedToId IS NULL OR rr.requestedTo.id=:requestedToId) AND (:cancelledById IS NULL OR rr.cancelledBy.id=:cancelledById) AND (:assignedToId IS NULL OR rr.assignedTo.id=:assignedToId) AND (:householdId IS NULL OR rr.household.id=:householdId) AND (rr.checkOut=:checkOut) AND rr.deleted=FALSE")
    fun searchOut(
        @Param("q") query: String?, pageable: Pageable,
        @Param("createdBy") createdBy: String?,
        @Param("status") status: RequestStatuses?,
        @Param("requestedById") requestedById: Long?,
        @Param("requestedToId") requestedToId: Long?,
        @Param("cancelledById") cancelledById: Long?,
        @Param("assignedToId") assignedToId: Long?,
        @Param("householdId") householdId: Long?,
        @Param("checkOut") checkOut: Instant
    ): Page<RentalRequest>

    @Query("SELECT rr FROM RentalRequest rr WHERE (:q IS NULL OR LOWER(rr.createdBy) LIKE %:q%) AND (:createdBy IS NULL OR rr.createdBy=:createdBy) AND (:status IS NULL OR rr.status=:status) AND (:requestedById IS NULL OR rr.requestedBy.id=:requestedById) AND (:requestedToId IS NULL OR rr.requestedTo.id=:requestedToId) AND (:cancelledById IS NULL OR rr.cancelledBy.id=:cancelledById) AND (:assignedToId IS NULL OR rr.assignedTo.id=:assignedToId) AND (:householdId IS NULL OR rr.household.id=:householdId) AND rr.deleted=FALSE")
    fun search(
        @Param("q") query: String?, pageable: Pageable,
        @Param("createdBy") createdBy: String?,
        @Param("status") status: RequestStatuses?,
        @Param("requestedById") requestedById: Long?,
        @Param("requestedToId") requestedToId: Long?,
        @Param("cancelledById") cancelledById: Long?,
        @Param("assignedToId") assignedToId: Long?,
        @Param("householdId") householdId: Long?
    ): Page<RentalRequest>

    @Query("SELECT rr FROM RentalRequest rr WHERE rr.id=:id AND rr.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<RentalRequest>

}
