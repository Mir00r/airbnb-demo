package com.airbnb.rentalsearch.domains.reviews_ratings.models.mappers


import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.authenticator.config.security.SecurityContext
import com.airbnb.authenticator.domains.users.models.entities.User
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.households.models.entities.Household
import com.airbnb.rentalprocessor.domains.households.services.HouseholdService
import com.airbnb.rentalsearch.domains.reviews_ratings.models.dtos.ReviewDto
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReviewMapper @Autowired constructor(
    private val ratingMapper: RatingMapper,
    private val householdService: HouseholdService
) : BaseMapper<Review, ReviewDto> {

    override fun map(entity: Review): ReviewDto {
        val dto = ReviewDto();
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.title = entity.title
        dto.content = entity.content
        dto.householdId = entity.household.id ?: 0
        dto.ratings = entity.ratings.map { this.ratingMapper.map(it) }
        dto.postedByName = entity.user.name
        dto.averageRating = entity.averageRating

        return dto
    }

    override fun map(dto: ReviewDto, exEntity: Review?): Review {
        val review = exEntity ?: Review()

        review.title = dto.title
        review.content = dto.content
        review.household =
            this.householdService.find(dto.householdId)
                .orElseThrow { ExceptionUtil.notFound(Household::class.java, dto.householdId) }
        review.user = User(SecurityContext.getCurrentUser())
        review.ratings = dto.ratings
            .map {
                val rating = this.ratingMapper.map(it, null)
                rating.review = review
                rating
            }
        review.averageRating = review.ratings.map { it.value }.average()
        return review
    }

}
