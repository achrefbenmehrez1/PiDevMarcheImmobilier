package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}