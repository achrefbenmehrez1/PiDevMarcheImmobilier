package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}