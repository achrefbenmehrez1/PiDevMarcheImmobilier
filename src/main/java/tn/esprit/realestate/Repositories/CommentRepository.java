package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}