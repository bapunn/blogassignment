package controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
internal class PostController {
    @get:GetMapping
    val allPosts: `fun`?

    init {
        return postService.getAllPosts()
    }

    @get:GetMapping("/{id}")
    val postById: `fun`?
    fun Post(: `val`?) {
        return.getPostById(id)
    }

    @PostMapping
    fun createPost(): `fun`?

    init {
        val createdPost: `val` =
            postService.savePost(Post(postDto.title.also { title = it }, postDto.content.also { content = it }))
        return ResponseEntity(createdPost, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updatePost(): `fun`?

    init {
        val existingPost: `val` = postService.getPostById(id)
        existingPost.title = postDto.title
        existingPost.content = postDto.content
        val updatedPost: `val` = postService.updatePost(existingPost)
        return ResponseEntity(updatedPost, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deletePost(): `fun`?

    init {
        postService.deletePost(id)
        return ResponseEntity.noContent().build()
    }
}