package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Forum.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {

    public List<Post> getAllPosts();

    public Post getPostById(Long id);

    public void deletePost(Long id);

    public Post updatePost(Long id, Post updatedPost);

    public List<Post> getPostsByAuthor(Long authorId);

    public List<Post> searchPostsByTitle(String title);

    public List<Post> getPostsByTag(String tag);

    public List<Post> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Post> getLatestPosts(int count);

    public List<Post> getMostPopularPosts(int count);

    public void incrementViewsCount(Long postId);

    public List<Post> getRelatedPosts(Long postId);
}
