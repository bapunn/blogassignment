package dto.responseDto

import java.time.LocalDateTime
import model.Post
class PostResponseDto(post: Post) {
    private val id: Long
    private val title: String
    private var content: String? = null
    private val userId: Long
    private val nickname: String
    private val createdAt: LocalDateTime
    private val view: Int
    private val totalComment: Int
    private val totalPage = 0

    init {
        id = post.getId()
        title = post.getTitle()
        nickname = post.getUser().getNickname()
        createdAt = post.getCreatedAt()
        view = post.getView()
        totalComment = post.getComments().size()
        userId = post.getUser().getId()
    }

    fun setContent(content: String?) {
        this.content = content
    }
}