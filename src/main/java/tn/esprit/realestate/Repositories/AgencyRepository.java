package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
}