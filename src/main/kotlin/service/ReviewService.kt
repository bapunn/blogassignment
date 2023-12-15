package service

import dto.requestDto.ReviewRequestDto
import repository.PostRepository
import repository.ReviewRepository

@RequiredArgsConstructor
@Service
class ReviewService {
    private val reviewRepository: ReviewRepository? = null
    private val postRespoistory: PostRepository? = null

    //리뷰 생성
    fun createReview(reviewRequestDto: ReviewRequestDto, id: Long?): ResponseEntity {
        val post : Post = postRespoistory.findById(id).orElseThrow { IllegalArgumentException("존재하지 않는 게시글입니다.") }
        println("coffee 검색")
        val review = Review(
            reviewRequestDto.getReview(),
            post,
            userDetails.getUser()
        )
        println("review 생성")
        reviewRepository.save(review)
        val reviewResponseDto: ReviewResponseDto = ReviewResponseDto.builder()
            .id(review.getId())
            .review(review.getReview())
            .nickname(review.getUser().getNickname())
            .createdAt(review.getCreatedAt())
            .build()
        println("코멘트 성공")
        return ResponseEntity.ok().body(reviewResponseDto)
    }

    //댓글 조회
    fun findReviews(id: Long?): ResponseEntity {
        val reviews: List<Review> = reviewRepository.findAllByPostIdOrderByCreatedAtDesc(id)
        val reviewResponseDtos: MutableList<ReviewResponseDto> = ArrayList<ReviewResponseDto>()
        for (review in reviews) {
            val reviewResponseDto: ReviewResponseDto = ReviewResponseDto.builder()
                .id(review.getId())
                .review(review.getReview())
                .nickname(review.getUser().getNickname())
                .createdAt(review.getCreatedAt())
                .build()
            reviewResponseDtos.add(reviewResponseDto)
        }
        println("코멘트 검색 성공")
        return ResponseEntity.ok().body(reviewResponseDtos)
    }

    //삭제
    fun deleteReview(reviewId: Long, userDetails: UserDetailsImpl): ResponseEntity {
        //삭제할 댓글이 있는지 확인
        val review: Review =
            reviewRepository.findById(reviewId).orElseThrow { IllegalArgumentException("존재하지 않는 리뷰입니다.") }
        //삭제할 댓글의 유저와 현재 로그인한 유저가 같은지 확인
        require(review.getUser().getNickname().equals(userDetails.getUser().getNickname())) { "자신의 리뷰만 삭제할 수 있습니다." }
        println("delete reviewId : $reviewId")
        reviewRepository.deleteById(reviewId)
        println("코멘트 삭제 성공")
        return ResponseEntity.noContent().build()
    }

    //댓글 수정
    @Transactional
    fun updateReview(
        reviewId: Long?,
        reviewRequestDto: ReviewRequestDto,
        userDetails: UserDetailsImpl
    ): ResponseEntity {
        val review: Review =
            reviewRepository.findById(reviewId).orElseThrow { IllegalArgumentException("아이디가 존재하지 않습니다.") }
        if (!review.getUser().getNickname().equals(userDetails.getUser().getNickname())) {
            review.update(reviewRequestDto)
            throw IllegalArgumentException("자신의 댓글만 수정할 수 있습니다.")
        }
        val reviewResponseDto: ReviewResponseDto = ReviewResponseDto.builder()
            .id(review.getId())
            .review(reviewRequestDto.getReview())
            .nickname(review.getUser().getNickname())
            .createdAt(review.getCreatedAt())
            .build()
        review.update(reviewRequestDto)
        return ResponseEntity.ok().body(reviewResponseDto)
    }
}