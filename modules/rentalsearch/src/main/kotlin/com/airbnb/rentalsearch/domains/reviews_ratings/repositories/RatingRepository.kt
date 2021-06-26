package com.airbnb.rentalsearch.domains.reviews_ratings.repositories

import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.id=:id AND r.deleted=false ")
    fun find(@Param("id") id: Long): Optional<Rating>

    @Query("SELECT r FROM Rating r WHERE r.id=:ratingId AND r.review.id=:reviewId AND r.deleted=false ")
    fun find(@Param("reviewId") reviewId: Long, @Param("ratingId") ratingId: Long): Optional<Rating>

    @Query("SELECT r FROM Rating r WHERE r.review.id=:reviewId AND r.deleted=false ")
    fun findByReview(@Param("reviewId") reviewId: Long): Optional<Rating>

    @Query("SELECT AVG (r.value) FROM Rating r WHERE r.review.id=:reviewId AND r.deleted=false")
    fun calculateAverageRatingForReview(@Param("reviewId") reviewId: Long): Double

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.review.id=:reviewId")
    fun deleteRatings(@Param("reviewId") reviewId: Long)
}
