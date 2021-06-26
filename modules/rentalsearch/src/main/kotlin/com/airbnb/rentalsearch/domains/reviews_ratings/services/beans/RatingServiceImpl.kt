package com.airbnb.rentalsearch.domains.reviews_ratings.services.beans

import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Rating
import com.airbnb.rentalsearch.domains.reviews_ratings.repositories.RatingRepository
import com.example.astha.domains.reviews.services.RatingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class RatingServiceImpl @Autowired constructor(
    private val ratingRepository: RatingRepository
) : RatingService {

    override fun find(id: Long): Optional<Rating> {
        return this.ratingRepository.find(id)
    }

    override fun find(reviewId: Long, ratingId: Long): Optional<Rating> {
        return this.ratingRepository.find(reviewId, ratingId)
    }

    @Transactional
    override fun save(rating: Rating): Rating {
        val exRating = this.ratingRepository.findByReview(rating.review.id ?: 0)

        return if (exRating.isPresent) {
            val r = exRating.get()
            r.value = rating.value
            ratingRepository.save(r)
        } else ratingRepository.save(rating)
    }

}
