package com.airbnb.rentalsearch.domains.reviews_ratings.services.beans


import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.common.utils.PageAttr
import com.airbnb.rentalprocessor.domains.households.repositories.HouseholdRepository
import com.airbnb.rentalprocessor.domains.rentalrequests.models.enums.RequestStatuses
import com.airbnb.rentalprocessor.domains.rentalrequests.repositories.RentalRequestRepository
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Review
import com.airbnb.rentalsearch.domains.reviews_ratings.repositories.RatingRepository
import com.airbnb.rentalsearch.domains.reviews_ratings.repositories.ReviewRepository
import com.airbnb.rentalsearch.domains.reviews_ratings.services.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
open class ReviewServiceImpl @Autowired constructor(
    private val reviewRepo: ReviewRepository,
    private val ratingRepo: RatingRepository,
    private val rentalRequestRepository: RentalRequestRepository
) : ReviewService {

    override fun search(productId: Long, query: String, page: Int, size: Int): Page<Review> {
        return this.reviewRepo.search(productId, query, PageAttr.getPageRequest(page, size))
    }

    override fun search(query: String, page: Int, size: Int): Page<Review> {
        return this.reviewRepo.search(query, PageAttr.getPageRequest(page, size))
    }

    @Transactional
    override fun save(entity: Review): Review {
        this.validate(entity)

        if (entity.id != null)
            this.ratingRepo.deleteRatings(entity.id ?: 0)

        entity.averageRating = entity.ratings.map { it.value }.average()
        return reviewRepo.save(entity)
    }

    override fun validate(entity: Review) {
        if (entity.isNew()) {
            val rentalRequest = this.rentalRequestRepository.find(
                entity.household.id ?: 0,
                entity.user.id ?: 0,
                RequestStatuses.VISITED
            )
            if (rentalRequest.isEmpty()) throw ExceptionUtil.invalid("Did not find any rental request so you are unable to submit review in this household")
            if (rentalRequest[0].checkOut.isAfter(Instant.now())) throw ExceptionUtil.invalid("Unable to provide review because you are still renting this household")
        }
    }

    override fun find(id: Long): Optional<Review> {
        return this.reviewRepo.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (!softDelete) {
            this.reviewRepo.deleteById(id)
            return
        }
        val review = this.find(id).orElseThrow { ExceptionUtil.notFound(Review::class.java, id) }
        review.deleted = true
        this.save(review)
    }

}
