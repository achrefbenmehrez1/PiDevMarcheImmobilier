package tn.esprit.realestate.Controllers.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.Entities.Tag;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.TagRepository;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Services.Forum.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @PostMapping
    public Boolean createPost(@RequestBody Post post) {
        User author = userRepository.getOne(post.getAuthor().getId());
        post.setAuthor(author);

        List<Tag> tags = new ArrayList<>();
        for (Tag tag : post.getTags()) {
            Tag loadedTag = tagRepository.getOne(tag.getId());
            tags.add(loadedTag);
        }
        post.setTags(tags);

        // Save the post object
        Post savedPost = postService.createPost(post);
        return true;
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(@RequestParam(required = false) String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestPosts(@RequestParam(required = false, defaultValue = "10") int n) {
        List<Post> posts = postService.getLatestPosts(n);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tags/{tagName}")
    public ResponseEntity<?> getPostsByTag(@PathVariable String tagName) {
        List<Post> posts = postService.getPostsByTag(tagName);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        postService.updatePost(id, updatedPost);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
