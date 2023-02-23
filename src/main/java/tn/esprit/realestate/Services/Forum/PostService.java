package tn.esprit.realestate.Services.Forum;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.IServices.Forum.IPostService;
import tn.esprit.realestate.Repositories.PostRepository;
import tn.esprit.realestate.Repositories.TagRepository;

import java.util.Date;
import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public Post createPost(Post post) {
        post.setCreationDate(new Date());
        post.setNumberOfComments(0);
        post.setNumberOfReactions(0);

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByTag(String tagName) {
        return postRepository.findByTagsName(tagName);
    }

    public List<Post> getLatestPosts(int n) {
        return postRepository.findLatestPosts(n);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleLikeAndContentLike(keyword);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void updatePost(Long id, Post updatedPost) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setTags(updatedPost.getTags());
            postRepository.save(post);
        }
    }
}
