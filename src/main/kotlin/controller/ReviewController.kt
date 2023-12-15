package controller

import dto.requestDto.ReviewRequestDto
import service.ReviewService

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
class ReviewController {
    private val reviewService: ReviewService? = null

    //댓글수정
    @PutMapping("/posts/{id}/reviews/{reviewid}")
    fun updateReview(
        @PathVariable reviewid: Long?, @RequestBody reviewRequestDto: ReviewRequestDto?
    ): ResponseEntity {
        return reviewService.updateReview(reviewid, reviewRequestDto)
    }

    //댓글삭제
    @DeleteMapping("/posts/{id}/reviews/{reviewid}")
    fun deleteReview(
        @PathVariable reviewid: Long?,
        @AuthenticationPrincipal userDetails: UserDetailsImpl?
    ): ResponseEntity {
        reviewService.deleteReview(reviewid, userDetails)
        return ResponseEntity.ok().body(reviewid)
    }

    //댓글등록
    @PostMapping("posts/{id}/reviews")
    fun createReview(
        @RequestBody reviewRequestDto: ReviewRequestDto?, @AuthenticationPrincipal userDetails: UserDetailsImpl?,
        @PathVariable id: Long?
    ): ResponseEntity {
        return reviewService.createReview(reviewRequestDto, id, userDetails)
    }

    //댓글조회
    @GetMapping("posts/{id}/reviews")
    fun viewReview(@PathVariable id: Long?): ResponseEntity {
        return reviewService.findReviews(id)
    }
}