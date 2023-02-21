package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}