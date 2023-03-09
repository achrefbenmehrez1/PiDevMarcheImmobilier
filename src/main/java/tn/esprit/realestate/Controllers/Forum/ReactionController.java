package tn.esprit.realestate.Controllers.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reaction;
import tn.esprit.realestate.Services.Forum.ReactionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @GetMapping
    public List<Reaction> getAllReactions() {
        return reactionService.getAllReactions();
    }

    @GetMapping("/{id}")
    public Reaction getReactionById(@PathVariable Long id) {
        return reactionService.getReactionById(id);
    }

    @PostMapping("/post/{postId}")
    public Reaction reactToPost(@PathVariable Long postId, @RequestBody Reaction reaction) {
        return reactionService.reactToPost(postId, reaction);
    }

    @PostMapping("/comment/{commentId}")
    public Reaction reactToComment(@PathVariable Long commentId, @RequestBody Reaction reaction) {
        return reactionService.reactToComment(commentId, reaction);
    }

    @PostMapping("/reply/{replyId}")
    public Reaction reactToReply(@PathVariable Long replyId, @RequestBody Reaction reaction) {
        return reactionService.reactToReply(replyId, reaction);
    }

    @PutMapping("/{id}")
    public Reaction updateReaction(@PathVariable Long id, @RequestBody Reaction reaction) {
        return reactionService.updateReaction(id, reaction);
    }

    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
    }

    // get reactions by post id
    @GetMapping("/post/{postId}")
    public List<Reaction> getReactionsByPost(@PathVariable Long postId) {
        return reactionService.getReactionsByPost(postId);
    }

    // get reactions by comment id
    @GetMapping("/comment/{commentId}")
    public List<Reaction> getReactionsByComment(@PathVariable Long commentId) {
        return reactionService.getReactionsByComment(commentId);
    }

    // get reactions by reply id
    @GetMapping("/reply/{replyId}")
    public List<Reaction> getReactionsByReply(@PathVariable Long replyId) {
        return reactionService.getReactionsByReply(replyId);
    }

    // get reactions by date range
    @GetMapping("/date-range")
    public List<Reaction> getReactionsByDateRange(
            @RequestParam(name = "start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reactionService.getReactionsByDateRange(startDate, endDate);
    }

    // get most reacted posts
    @GetMapping("/most-reacted-posts")
    public List<Post> getMostReactedPosts(@RequestParam int count) {
        return reactionService.getMostReactedPosts(count);
    }
}
