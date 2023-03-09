package tn.esprit.realestate.IServices.Forum;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Forum.Reply;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IReplyService {

    public List<Reply> getAllReplies();

    public Reply getReplyById(Long id);

    public ResponseEntity<String> createReply(Optional<MultipartFile> file, String content, Long commentId) throws MessagingException;

    public Reply updateReply(Long id, Optional<MultipartFile> file, Optional<String> content);

    public void deleteReply(Long id);

    public List<Reply> getRepliesByAuthor(Long authorId);

    public List<Reply> getRepliesByComment(Long commentId);

    public List<Reply> getRepliesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Reply> getLatestReplies(int count);

    public List<Reply> getRepliesWithMostReactions();
}