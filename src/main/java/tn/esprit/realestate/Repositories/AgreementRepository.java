package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Agreement;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}