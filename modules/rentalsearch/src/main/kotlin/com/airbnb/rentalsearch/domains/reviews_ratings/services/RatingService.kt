package com.example.astha.domains.reviews.services

import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Rating
import java.util.*

interface RatingService {
    fun find(id: Long): Optional<Rating>
    fun find(reviewId: Long, ratingId: Long): Optional<Rating>
    fun save(rating: Rating): Rating
}
