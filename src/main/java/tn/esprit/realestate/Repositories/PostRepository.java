package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTagsName(String tagName);

    @Query("SELECT p FROM Post p ORDER BY p.creationDate DESC")
    List<Post> findLatestPosts(int n);

    @Query("select p from Post p where p.title like ?1 or p.content like ?1")
    List<Post> findByTitleLikeAndContentLike(String keyword);
}