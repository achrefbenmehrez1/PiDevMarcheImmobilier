package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean findByName(String name);
}