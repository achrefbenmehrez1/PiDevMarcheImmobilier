package tn.esprit.realestate.Repositories.Forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Reply;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor_Id(Long authorId);

    List<Comment> findByPost_Id(Long postId);

    List<Comment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    List<Comment> findTopNByOrderByCreatedAtDesc(@Param("n") int n);

    @Query("SELECT r FROM Reply r WHERE r.comment.id = :commentId")
    List<Reply> findRepliesByCommentId(@Param("commentId") Long commentId);

    @Query("SELECT c FROM Comment c ORDER BY SIZE(c.replies) DESC")
    List<Comment> findAllOrderByRepliesDesc();
}