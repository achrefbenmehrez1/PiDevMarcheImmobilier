package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}