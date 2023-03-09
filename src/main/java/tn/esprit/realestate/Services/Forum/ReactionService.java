package tn.esprit.realestate.Services.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reaction;
import tn.esprit.realestate.Entities.Forum.Reply;
import tn.esprit.realestate.IServices.Forum.IReactionService;
import tn.esprit.realestate.Repositories.Forum.CommentRepository;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.ReactionRepository;
import tn.esprit.realestate.Repositories.Forum.ReplyRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReactionService implements IReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll();
    }

    public Reaction getReactionById(Long id) {
        Optional<Reaction> reaction = reactionRepository.findById(id);
        if (reaction.isPresent()) {
            return reaction.get();
        } else {
            return null;
        }
    }

    public Reaction reactToPost(Long postId, Reaction reaction) {
        Post post = postRepository.findById(postId).orElse(null);
        reaction.setPost(post);
        reaction.setCreatedAt(LocalDateTime.now());
        return reactionRepository.save(reaction);
    }

    public Reaction reactToComment(Long commentId, Reaction reaction) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        reaction.setComment(comment);
        reaction.setCreatedAt(LocalDateTime.now());
        return reactionRepository.save(reaction);
    }

    public Reaction reactToReply(Long replyId, Reaction reaction) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        reaction.setReply(reply);
        reaction.setCreatedAt(LocalDateTime.now());
        return reactionRepository.save(reaction);
    }

    public Reaction updateReaction(Long id, Reaction reaction) {
        Reaction existingReaction = getReactionById(id);
        existingReaction.setType(reaction.getType());
        return reactionRepository.save(existingReaction);
    }

    public void deleteReaction(Long id) {
        Reaction reaction = getReactionById(id);
        reactionRepository.delete(reaction);
    }

    @Override
    public List<Reaction> getReactionsByPost(Long postId) {
        return reactionRepository.findByPost_Id(postId);
    }

    @Override
    public List<Reaction> getReactionsByComment(Long commentId) {
        return reactionRepository.findByComment_Id(commentId);
    }

    @Override
    public List<Reaction> getReactionsByReply(Long replyId) {
        return reactionRepository.findByReply_Id(replyId);
    }

    @Override
    public List<Reaction> getReactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return reactionRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Post> getMostReactedPosts(int count) {
        return postRepository.getMostReactedPosts(count);
    }
}
