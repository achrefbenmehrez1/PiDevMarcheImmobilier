package tn.esprit.realestate.Services.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Comment;
import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.IServices.Forum.ICommentService;
import tn.esprit.realestate.Repositories.CommentRepository;
import tn.esprit.realestate.Repositories.PostRepository;

import java.util.Date;
import java.util.List;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public Comment createComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            comment.setPost(post);
            comment.setCreationDate(new Date());
            post.setNumberOfComments(post.getNumberOfComments() + 1);
            postRepository.save(post);
            return commentRepository.save(comment);
        }
        return null;
    }

    public List<Comment> getAllCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreationDateDesc(postId);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            Post post = comment.getPost();
            post.setNumberOfComments(post.getNumberOfComments() - 1);
            postRepository.save(post);
            commentRepository.deleteById(id);
        }
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment.setText(updatedComment.getText());
            commentRepository.save(comment);
        }
        return comment;
    }
}
