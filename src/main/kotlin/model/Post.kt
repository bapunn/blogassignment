package model

import dto.requestDto.PostRequestDto
import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
class Post {
    // 번호
    @Id
    private val id: Long? = null

    // 제목
    @Column(nullable = false)
    private var title: String? = null

    // 게시글 내용(길이 제한 및 널러블)
    @Column(length = 2000, nullable = false)
    private var content: String? = null

    // 작성 시간
    @Column(nullable = false)
    private val createdAt: LocalDateTime? = null

    // 리뷰 목록 (1:N 관계, 게시글과 연관된 리뷰 목록)
    @OneToMany(mappedBy = "post", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val reviews: MutableList<Review> = mutableListOf()

    // 트랜잭션 내에서 게시글 정보를 설정하고 연관된 리뷰를 생성하여 연결하는 메서드
    @Transactional
    fun setPost(postRequestDto: PostRequestDto) {
        // 게시글 정보 설정
        title = postRequestDto.title
        content = postRequestDto.content

        // Review 생성 및 연결
        val review = Review(content = postRequestDto.content, post = this)
        reviews.add(review)
    }
}
