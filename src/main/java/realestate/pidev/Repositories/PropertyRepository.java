package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
}