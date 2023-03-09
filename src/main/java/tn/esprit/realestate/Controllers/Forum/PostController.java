package tn.esprit.realestate.Controllers.Forum;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.Builder.Default;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.detectlanguage.errors.APIError;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import tn.esprit.realestate.Dto.Forum.PostDto;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Repositories.Forum.AttachmentRepository;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.TagRepository;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Services.Forum.PostService;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("tags") List<String> tagNames)
            throws IOException, MessagingException, EmailException, GeoIp2Exception, APIError {
        return postService.createPost(file, title, content, tagNames);
    }

    @PutMapping("/approve/{postId}")
    public ResponseEntity<Void> approvePost(@PathVariable Long postId) throws MessagingException {
        postService.approvePost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unapproved")
    public ResponseEntity<List<PostDto>> getUnapprovedPosts() {
        List<PostDto> unapprovedPosts = postService.getUnapprovedPosts();
        return ResponseEntity.ok(unapprovedPosts);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("title") Optional<String> title,
            @RequestParam("content") Optional<String> content,
            @RequestParam("tags") Optional<List<String>> tagNames) throws IOException {
        return postService.updatePost(id, file, title, content, tagNames);
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
    public List<Post> getPostsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
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
    public List<Post> getAllPosts(@RequestParam(defaultValue = "en") String translateTo) {
        return postService.getAllPosts(translateTo);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    /*
     * @GetMapping("/search/{keyword}")
     * public List<Post> search(@PathVariable String keyword) {
     * return postService.search(keyword);
     * }
     */
}
