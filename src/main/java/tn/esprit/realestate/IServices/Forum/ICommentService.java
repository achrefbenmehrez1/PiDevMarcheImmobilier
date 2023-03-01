package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface ICommentService {

    public List<Comment> getAllComments();

    public Comment getCommentById(Long id);

    public Comment createComment(Comment comment);

    public Comment updateComment(Long id, Comment comment);

    public boolean deleteComment(Long id);

    public List<Comment> getCommentsByAuthor(Long authorId);

    public List<Comment> getCommentsByPost(Long postId);

    public List<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Comment> getLatestComments(int count);

    public List<Post> getMostCommentedPosts();
}
