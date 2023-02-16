package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Agreement;

public interface AgreementRepository extends JpaRepository<Agreement, Integer> {
}