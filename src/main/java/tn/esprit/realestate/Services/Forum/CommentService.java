package tn.esprit.realestate.Services.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reply;
import tn.esprit.realestate.IServices.Forum.ICommentService;
import tn.esprit.realestate.Repositories.Forum.CommentRepository;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.ReplyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            return null;
        }
    }

    public Comment createComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(userRepository.findById(comment.getAuthor().getId()).get());
        comment.setPost(postRepository.findById(comment.getPost().getId()).get());
        comment.setReactions(new ArrayList<>());
        return commentRepository.save(comment);
    }

    public boolean deleteComment(Long id) {
        Comment comment = getCommentById(id);
        if(comment != null) {
            commentRepository.delete(comment);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Comment> getCommentsByAuthor(Long authorId) {
        return commentRepository.findByAuthor_Id(authorId);
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPost_Id(postId);
    }

    @Override
    public List<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return commentRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Comment> getLatestComments(int count) {
        return commentRepository.findTopNByOrderByCreatedAtDesc(count);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(updatedComment.getContent());
            comment.setUpdatedAt(LocalDateTime.now()); // set updated_at to current date and time
            return commentRepository.save(comment);
        } else {
            return null;
        }
    }

    public List<Post> getMostCommentedPosts() {
        List<Post> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparingInt(post -> post.getComments().size()));
        Collections.reverse(posts);
        return posts;
    }

    public List<Comment> getCommentsWithMostReactions() {
        List<Comment> comments = commentRepository.findAll();
        Collections.sort(comments, Comparator.comparingInt(comment -> comment.getReactions().size()));
        Collections.reverse(comments);
        return comments;
    }

    public List<Reply> findRepliesByCommentId(Long commentId) {
        return commentRepository.findRepliesByCommentId(commentId);}

    public List<Comment> getMostRepliedComments() {
        return commentRepository.findAllOrderByRepliesDesc();
    }

    public Comment getParentCommentForReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        return reply.getComment();
    }
}
