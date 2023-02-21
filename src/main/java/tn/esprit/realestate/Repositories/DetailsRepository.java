package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Details;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long> {
}