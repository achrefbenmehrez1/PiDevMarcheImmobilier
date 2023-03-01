package tn.esprit.realestate.Repositories.Forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reply;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByAuthor_Id(Long authorId);

    List<Reply> findByComment_Id(Long commentId);

    List<Reply> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c FROM Post c ORDER BY c.createdAt DESC")
    List<Reply> findTopNByOrderByCreatedAtDesc(@Param("n") int n);

    @Query("SELECT r FROM Reply r ORDER BY SIZE(r.reactions) DESC")
    List<Reply> findAllOrderByReactionsDesc();
}