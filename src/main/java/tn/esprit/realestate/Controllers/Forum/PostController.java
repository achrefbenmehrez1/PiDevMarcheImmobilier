package tn.esprit.realestate.Controllers.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Repositories.Forum.TagRepository;
import tn.esprit.realestate.Services.Forum.PostService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private TagRepository tagRepository;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @GetMapping("/author/{id}")
    public List<Post> getPostsByAuthor(@PathVariable Long id) {
        return postService.getPostsByAuthor(id);
    }

    @GetMapping("/search")
    public List<Post> searchPostsByTitle(@RequestParam String title) {
        return postService.searchPostsByTitle(title);
    }

    @GetMapping("/tag/{tag}")
    public List<Post> getPostsByTag(@PathVariable String tag) {
        return postService.getPostsByTag(tag);
    }

    @GetMapping("/date")
    public List<Post> getPostsByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return postService.getPostsByDateRange(startDate, endDate);
    }

    @GetMapping("/latest/{count}")
    public List<Post> getLatestPosts(@PathVariable int count) {
        return postService.getLatestPosts(count);
    }

    @GetMapping("/popular/{count}")
    public List<Post> getMostPopularPosts(@PathVariable int count) {
        return postService.getMostPopularPosts(count);
    }

    @GetMapping("/tags/{keyword}")
    public List<Post> getTagsByKeyword(@PathVariable String keyword) {
        return postService.getRelatedPosts(tagRepository.findByName(keyword).getId());
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }
}

