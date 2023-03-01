package tn.esprit.realestate.Repositories.Forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Forum.Reaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPost_Id(Long postId);

    List<Reaction> findByComment_Id(Long commentId);

    List<Reaction> findByReply_Id(Long replyId);

    List<Reaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}