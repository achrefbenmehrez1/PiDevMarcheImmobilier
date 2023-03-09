package tn.esprit.realestate.Repositories.Forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Forum.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    List<Tag> findByPosts_Id(Long postId);

    List<Tag> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t, COUNT(p) AS tagCount FROM Tag t JOIN t.posts p GROUP BY t ORDER BY tagCount DESC")
    List<Object[]> findMostUsedTags(int count);
}