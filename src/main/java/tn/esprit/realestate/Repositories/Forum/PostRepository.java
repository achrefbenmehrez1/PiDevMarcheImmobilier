package tn.esprit.realestate.Repositories.Forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String title);

    List<Post> findByTags_Name(String tag);

    List<Post> findByAuthor_Id(Long authorId);

    List<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c FROM Post c ORDER BY c.createdAt DESC")
    List<Post> findTopNByOrderByCreatedAtDesc(@Param("n") int n);

    @Query("SELECT c FROM Post c ORDER BY c.views DESC")
    List<Post> findTopNByOrderByViewsDesc(@Param("n") int n);

    @Query("SELECT p FROM Post p JOIN p.reactions r GROUP BY p.id ORDER BY COUNT(r.id) DESC")
    List<Post> getMostReactedPosts(@Param("count") int count);

    List<Post> findByTagsContaining(Tag tag);

    List<Post> findByApprovedFalse();
}