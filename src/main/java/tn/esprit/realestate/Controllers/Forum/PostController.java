package tn.esprit.realestate.Controllers.Forum;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
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
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/user/ip")
    public String getUserIp(HttpServletRequest request) throws UnknownHostException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        InetAddress inetAddress = InetAddress.getByName(ip);
        String ipAddress = inetAddress.getHostAddress();

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://ip-api.com/json/" + ipAddress;
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);

        return "Your IP address is: " + ipAddress;
    }

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestParam("file") Optional<MultipartFile> file,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("tags") List<String> tagNames) throws IOException, MessagingException {
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

    /*@GetMapping("/search/{keyword}")
    public List<Post> search(@PathVariable String keyword) {
        return postService.search(keyword);
    }*/
}

