package com.airbnb.rentalsearch.domains.reviews_ratings.repositories

import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE (:q IS NULL OR r.title LIKE %:q%) AND r.deleted=false")
    fun search(@Param("q") query: String?, pageable: Pageable): Page<Review>

    @Query("SELECT r FROM Review r WHERE r.household.id=:householdId AND (:q IS NULL OR r.title LIKE %:q%) AND r.deleted=false")
    fun search(@Param("householdId") householdId: Long, @Param("q") query: String?, pageable: Pageable): Page<Review>

    @Query("SELECT r FROM Review r WHERE r.id=:id AND r.deleted=false")
    fun find(@Param("id") id: Long): Optional<Review>

    @Query("SELECT AVG (r.averageRating) FROM Review r WHERE r.household=:household AND r.deleted=false")
    fun calculateAverageRatingForProduct(@Param("household") household: Household): Double

}
