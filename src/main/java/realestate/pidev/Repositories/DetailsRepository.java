package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Details;

public interface DetailsRepository extends JpaRepository<Details, Integer> {
}