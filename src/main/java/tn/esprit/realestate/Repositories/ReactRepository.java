package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.Entities.React;
import tn.esprit.realestate.Entities.User;

import java.util.List;

@Repository
public interface ReactRepository extends JpaRepository<React, Long> {
    List<React> findByPost(Post post);

    List<React> findByAuthor(User author);
}