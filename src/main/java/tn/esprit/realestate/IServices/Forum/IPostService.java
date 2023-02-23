package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Post;

import java.util.List;

public interface IPostService {

    public Post createPost(Post post);

    public List<Post> getAllPosts();

    public List<Post> getPostsByTag(String tagName);

    public List<Post> getLatestPosts(int limit);

    public List<Post> searchPosts(String keyword);

    public Post getPostById(Long id);

    public void deletePost(Long id);

    public void updatePost(Long id, Post updatedPost);

}
