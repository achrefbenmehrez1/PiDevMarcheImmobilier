package tn.esprit.realestate.Controllers.Forum;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reply;
import tn.esprit.realestate.Services.Forum.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<?> createComment(@RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("content") String content, @RequestParam Long postId) throws jakarta.mail.MessagingException, EmailException {
        return commentService.createComment(file, content, postId);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("content") Optional<String> content,
            @RequestParam("authorId") Optional<Long> authorId) {
        return commentService.updateComment(id, file, content, authorId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }

    @GetMapping("/byAuthor/{authorId}")
    public List<Comment> getCommentsByAuthor(@PathVariable Long authorId) {
        return commentService.getCommentsByAuthor(authorId);
    }

    @GetMapping("/byPost/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @GetMapping("/byDate")
    public List<Comment> getCommentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return commentService.getCommentsByDateRange(startDate, endDate);
    }

    @GetMapping("/latest/{count}")
    public List<Comment> getLatestComments(@PathVariable int count) {
        return commentService.getLatestComments(count);
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping("/mostCommentedPosts")
    public List<Post> getMostCommentedPosts() {
        return commentService.getMostCommentedPosts();
    }

    @GetMapping("/commentsWithMostReactions")
    public List<Comment> getCommentsWithMostReactions() {
        return commentService.getCommentsWithMostReactions();
    }

    @GetMapping("/findRepliesByCommentId/{id}")
    public List<Reply> findRepliesByCommentId(@PathVariable Long id) {
        return commentService.findRepliesByCommentId(id);
    }
}
