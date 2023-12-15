package model

import dto.requestDto.ReviewRequestDto
import jakarta.persistence.*

@Entity
class Review(
    // 리뷰 내용
    @field:Column(nullable = false)
    private var review: String,

    // 해당 리뷰가 속한 게시글
    @ManyToOne
    @JoinColumn(name = "post_id")
    private val post: Post,

    // 리뷰를 작성한 사용자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private val user: User
) {
    // 리뷰의 고유 식별자
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private val id: Long? = null

    // 생성자에서 게시글과 사용자를 초기화
    init {
        this.post = post
        this.user = user
    }

    // 리뷰 수정 메서드
    fun update(reviewRequestDto: ReviewRequestDto) {
        // ReviewRequestDto에서 새로운 리뷰 내용을 가져와 업데이트
        review = reviewRequestDto.getReview()
    }
}
