package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}