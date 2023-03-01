package tn.esprit.realestate.Services.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;
import tn.esprit.realestate.IServices.Forum.IPostService;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.TagRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            return null;
        }
    }

    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : post.getTags()) {
            Tag existingTag = tagRepository.findByName(tag.getName());
            if (existingTag != null) {
                tags.add(existingTag);
            } else {
                tags.add(tagRepository.save(tag));
            }
        }
        post.setTags(tags);
        Post createdPost = postRepository.save(post);
        return createdPost;
    }

    public Post updatePost(Long id, Post post) {
        Post existingPost = getPostById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setTags(post.getTags());
        existingPost.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(existingPost);
    }

    @Override
    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthor_Id(authorId);
    }

    @Override
    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTags_Name(tag);
    }

    @Override
    public List<Post> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Post> getLatestPosts(int count) {
        return postRepository.findTopNByOrderByCreatedAtDesc(count);
    }

    @Override
    public List<Post> getMostPopularPosts(int count) {
        return postRepository.findTopNByOrderByViewsDesc(count);
    }

    @Override
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Override
    public void incrementViewsCount(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    @Override
    public List<Post> getRelatedPosts(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<Tag> tags = post.getTags();
        List<Post> relatedPosts = new ArrayList<>();
        for (Tag tag : tags) {
            List<Post> postsWithTag = postRepository.findByTagsContaining(tag);
            for (Post p : postsWithTag) {
                if (!p.equals(post) && !relatedPosts.contains(p)) {
                    relatedPosts.add(p);
                }
            }
        }
        return relatedPosts;
    }
}