package tn.esprit.realestate.IServices.Forum;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ICommentService {

    public List<Comment> getAllComments();

    public Comment getCommentById(Long id);

    public ResponseEntity<String> createComment(Optional<MultipartFile> file, String content, Long postId) throws MessagingException;

    public Comment updateComment(Long id, Optional<MultipartFile> file, Optional<String> content, Optional<Long> authorId);

    public boolean deleteComment(Long id);

    public List<Comment> getCommentsByAuthor(Long authorId);

    public List<Comment> getCommentsByPost(Long postId);

    public List<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Comment> getLatestComments(int count);

    public List<Post> getMostCommentedPosts();
}
