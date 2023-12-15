package service

import repository.PostRepository
import model.Post
import dto.requestDto.PostRequestDto
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

class PostService {
    private val postRepository: PostRepository? = null

    // 게시글 저장
    fun savePost(postRequestDto: PostRequestDto) {
        // Post 엔터티 생성 및 저장
        val post: Post = Post.builder()
            .title(postRequestDto.getTitle())
            .content(postRequestDto.getContent())
            .createdAt(LocalDateTime.now())
            .user(userDetails.getUser())
            .build()
        postRepository.save(post)
        return ResponseEntity.ok().body("작성 완료")
    }

    // 게시글 수정
    @Transactional
    fun editPost(postRequestDto: PostRequestDto?, id: Long?) {
        val post: Optional<Post>? = postRepository.findById(id)
        if (post != null) {
            // 현재 사용자의 게시글인지 확인 후 수정
            if (!post.get().getUser().getNickname().getNickname())
                post.get().setPost(postRequestDto)
        }
        postRepository.save(post.get())
        val postResponseDto = PostResponseDto(post.get())
        postResponseDto.setContent(post.get().getContent())
        // 이후 처리
    }

    // 게시글 삭제
    @Transactional
    fun delPost(id: Long?, userDetails: UserDetailsImpl): ResponseEntity {
        val post: Optional<Post> = postRepository.findById(id)
        // 현재 사용자의 게시글인지 확인
        if (!post.get().getUser().getNickname().equals(userDetails.getUser().getNickname())) throw CustomException(
            ErrorCode.INVALID_AUTHORITY
        )
        postRepository.delete(post.get())
        return ResponseEntity.ok().body("삭제완료")
    }

    // 전체 게시글 조회
    fun getAll(pageable: Pageable?): ResponseEntity {
        val postList: Page<Post?> = postRepository.findAllByOrderByCreatedAtDesc(pageable)
        val response = HashMap<String, Any>()
        val postResponseDtos: List<PostResponseDto> = getPageDto(postList)
        response["post"] = postResponseDtos
        response["totalPage"] = postList.getTotalPages()
        return ResponseEntity.ok().body(response)
    }

    // 로그인한 사용자의 전체 게시글 조회
    fun getAllWithLogIn(userDetails: UserDetailsImpl, pageable: Pageable?): ResponseEntity {
        val postList: Page<Post?> = postRepository.findAllByOrderByCreatedAtDesc(pageable)
        val response = HashMap<String, Any>()
        val postResponseDtos: List<PostResponseDto> = getPageDtoWithLogIn(postList, userDetails)
        response["post"] = postResponseDtos
        response["totalPage"] = postList.getTotalPages()
        return ResponseEntity.ok().body(response)
    }

    // 게시글 상세 조회
    fun getDetail(id: Long?): ResponseEntity {
        val post: Optional<Post> = postRepository.findById(id)
        val postResponseDto = PostResponseDto(post.get())
        postResponseDto.setContent(post.get().getContent())
        return ResponseEntity.ok().body(postResponseDto)
    }

    // 로그인한 사용자의 게시글 상세 조회
    fun getDetailWithLogIn(id: Long?): ResponseEntity {
        val post: Optional<Post> = postRepository.findById(id)
        val postResponseDto = PostResponseDto(post.get())
        postResponseDto.setContent(post.get().getContent())

        // 추가적인 처리
        postResponseDto.setIsReport(id, "게시글")
    }

    // 페이지 DTO 변환
    fun getPageDto(postList: Page<Post?>): List<PostResponseDto> {
        return postList.stream()
            .map { post -> PostResponseDto(post) }
            .collect(Collectors.toList())
    }


    // 조회수 증가
    @Transactional
    fun addView(id: Long?) {
        postRepository.updateView(id)
    }
}
