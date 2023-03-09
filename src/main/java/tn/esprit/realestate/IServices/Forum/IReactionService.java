package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reaction;

import java.time.LocalDateTime;
import java.util.List;

public interface IReactionService {

    public List<Reaction> getAllReactions();

    public Reaction getReactionById(Long id);

    public Reaction reactToPost(Long postId, Reaction reaction);

    public Reaction reactToComment(Long commentId, Reaction reaction);

    public Reaction reactToReply(Long replyId, Reaction reaction);

    public Reaction updateReaction(Long id, Reaction reaction);

    public void deleteReaction(Long id);

    public List<Reaction> getReactionsByPost(Long postId);

    public List<Reaction> getReactionsByComment(Long commentId);

    public List<Reaction> getReactionsByReply(Long replyId);

    public List<Reaction> getReactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Post> getMostReactedPosts(int count);
}
