package repository;

import model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Query("update Post p set p.view = p.view + 1 where p.id = :id")
    int updateView(Long id);

    //내가 쓴 게시글 찾기
    List<Post> findAllByUserIdOrderByIdDesc(Long userId);

    //내가 쓴 게시글 모두 가져감 (마이페이지)
    List<Post> findAllByUserId(Long userId);

    Post findAllById(Long id);

}
