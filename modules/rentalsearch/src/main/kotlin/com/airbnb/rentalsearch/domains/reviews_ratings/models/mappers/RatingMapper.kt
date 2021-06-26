package com.airbnb.rentalsearch.domains.reviews_ratings.models.mappers


import com.airbnb.authenticator.common.mappers.BaseMapper
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalsearch.domains.reviews_ratings.models.dtos.RatingDto
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Rating
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Review
import com.airbnb.rentalsearch.domains.reviews_ratings.services.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RatingMapper @Autowired constructor(
    private val reviewService: ReviewService
): BaseMapper<Rating, RatingDto> {

    override fun map(entity: Rating): RatingDto {
        val dto = RatingDto();
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.value = entity.value
        dto.reviewId = entity.review.id ?: 0
        return dto
    }


    override fun map(dto: RatingDto, exEntity: Rating?): Rating {
        val rating = exEntity ?: Rating()
//        rating.review =
//            dto.reviewId.let {
//                this.reviewService.find(it).orElseThrow { ExceptionUtil.notFound(Review::class.java, it) }
//            }!!
        rating.value = dto.value
        return rating
    }
}
