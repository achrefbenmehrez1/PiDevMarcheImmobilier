package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Comment;

import java.util.List;

public interface ICommentService {
        public Comment createComment(Long postId, Comment comment);
        public List<Comment> getAllCommentsByPost(Long postId);
        public Comment getCommentById(Long id);
        public void deleteComment(Long id);
        public Comment updateComment(Long id, Comment updatedComment);
}
