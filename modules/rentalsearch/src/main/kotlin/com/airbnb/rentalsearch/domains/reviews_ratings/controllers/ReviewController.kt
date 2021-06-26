package com.airbnb.rentalsearch.domains.reviews_ratings.controllers


import com.airbnb.authenticator.common.controllers.BaseCrudController
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalsearch.domains.reviews_ratings.models.dtos.ReviewDto
import com.airbnb.rentalsearch.domains.reviews_ratings.models.entities.Review
import com.airbnb.rentalsearch.domains.reviews_ratings.models.mappers.ReviewMapper
import com.airbnb.rentalsearch.domains.reviews_ratings.services.ReviewService
import com.airbnb.rentalsearch.routing.Route
import com.airbnb.rentalsearch.utils.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(tags = [Constants.REVIEWS], description = Constants.REST_API)
class ReviewController @Autowired constructor(
    private val reviewService: ReviewService,
    private val reviewMapper: ReviewMapper
) : BaseCrudController<ReviewDto> {

    @GetMapping(Route.V1.SEARCH_REVIEWS)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.REVIEWS)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<ReviewDto>> {
        val reviews = this.reviewService.search(query, page, size)
        return ResponseEntity.ok(reviews.map { this.reviewMapper.map(it) })
    }

    @GetMapping(Route.V1.SEARCH_HOUSEHOLD_REVIEWS)
    @ApiOperation(value = Constants.SEARCH_ALL_MSG + Constants.REVIEWS)
    fun searchProductReviews(
        @PathVariable("product_id") productId: Long,
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<ReviewDto>> {
        val reviews = this.reviewService.search(productId, query, page, size)
        return ResponseEntity.ok(reviews.map { this.reviewMapper.map(it) })
    }

    @PostMapping(Route.V1.CREATE_REVIEWS)
    @ApiOperation(value = Constants.POST_MSG + Constants.REVIEWS)
    override fun create(@Valid @RequestBody dto: ReviewDto): ResponseEntity<ReviewDto> {
        val review = this.reviewService.save(this.reviewMapper.map(dto, null))
        return ResponseEntity.ok(this.reviewMapper.map(review))
    }

    @GetMapping(Route.V1.FIND_REVIEWS)
    @ApiOperation(value = Constants.GET_MSG + Constants.REVIEWS)
    override fun find(@PathVariable id: Long): ResponseEntity<ReviewDto> {
        val review = this.reviewService.find(id).orElseThrow { ExceptionUtil.notFound(Review::class.java, id) }
        return ResponseEntity.ok(this.reviewMapper.map(review))
    }

    @PatchMapping(Route.V1.UPDATE_REVIEWS)
    @ApiOperation(value = Constants.PATCH_MSG + Constants.REVIEWS)
    override fun update(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ReviewDto
    ): ResponseEntity<ReviewDto> {
        var review = this.reviewService.find(id).orElseThrow { ExceptionUtil.notFound(Review::class.java, id) }
        review = this.reviewService.save(this.reviewMapper.map(dto, review))
        return ResponseEntity.ok(this.reviewMapper.map(review))
    }

    @DeleteMapping(Route.V1.DELETE_REVIEWS)
    @ApiOperation(value = Constants.DELETE_MSG + Constants.REVIEWS)
    override fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        this.reviewService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
