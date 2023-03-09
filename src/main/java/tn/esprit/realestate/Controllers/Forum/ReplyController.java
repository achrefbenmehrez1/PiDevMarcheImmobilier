package tn.esprit.realestate.Controllers.Forum;

import jakarta.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Forum.Reply;
import tn.esprit.realestate.Services.Forum.ReplyService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public List<Reply> getAllReplies() {
        return replyService.getAllReplies();
    }

    @GetMapping("/{id}")
    public Reply getReplyById(@PathVariable Long id) {
        return replyService.getReplyById(id);
    }

    @PostMapping
    public ResponseEntity<String> createReply(@RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("content") String content, @RequestParam("commentId") Long commentId) throws MessagingException, EmailException {
        return replyService.createReply(file, content, commentId);
    }

    @PutMapping("/{id}")
    public Reply updateReply(@PathVariable Long id, @RequestParam("file") Optional<MultipartFile> file,
            @RequestParam("content") Optional<String> content) {
        return replyService.updateReply(id, file, content);
    }

    @DeleteMapping("/{id}")
    public void deleteReply(@PathVariable Long id) {
        replyService.deleteReply(id);
    }

    @GetMapping("/author/{id}")
    public List<Reply> getRepliesByAuthor(@PathVariable Long id) {
        return replyService.getRepliesByAuthor(id);
    }

    @GetMapping("/comment/{id}")
    public List<Reply> getRepliesByComment(@PathVariable Long id) {
        return replyService.getRepliesByComment(id);
    }

    @GetMapping("/date")
    public List<Reply> getRepliesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return replyService.getRepliesByDateRange(startDate, endDate);
    }

    @GetMapping("/latest/{count}")
    public List<Reply> getLatestReplies(@PathVariable int count) {
        return replyService.getLatestReplies(count);
    }
}
