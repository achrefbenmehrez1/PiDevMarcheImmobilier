package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Integer> {
}