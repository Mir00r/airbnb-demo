package com.airbnb.rentalsearch.domains.reviews_ratings.services

import com.airbnb.authenticator.common.services.BaseCrudService
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Review
import org.springframework.data.domain.Page

interface ReviewService : BaseCrudService<Review> {
    fun search(productId: Long, query: String, page: Int, size: Int): Page<Review>
}
