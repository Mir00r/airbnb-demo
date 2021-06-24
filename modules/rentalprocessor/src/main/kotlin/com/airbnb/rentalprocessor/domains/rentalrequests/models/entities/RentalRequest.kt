package com.airbnb.rentalprocessor.domains.rentalrequests.models.entities

import com.airbnb.authenticator.common.entities.BaseEntity
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "rental_requests", schema = "rentalprocessor")
class RentalRequest : BaseEntity() {

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    lateinit var status: RequestStatuses

    @Column(name = "note", columnDefinition = "LONGTEXT")
    var note: String? = null

    @ManyToOne
    @JoinColumn(name = "requested_by_id", nullable = false)
    lateinit var requestedBy: User

    @ManyToOne
    @JoinColumn(name = "requested_to_id", nullable = false)
    lateinit var requestedTo: User

    @ManyToOne
    @JoinColumn(name = "cancelled_by_id")
    var cancelledBy: User? = null

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    var assignedTo: User? = null

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    lateinit var household: Household

    @Column(name = "check_in", nullable = false)
    lateinit var checkIn: Instant

    @Column(name = "check_out", nullable = false)
    lateinit var checkOut: Instant
}
