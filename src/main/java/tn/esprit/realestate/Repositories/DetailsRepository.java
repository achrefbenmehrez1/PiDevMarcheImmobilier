package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Details;

public interface DetailsRepository extends JpaRepository<Details, Integer> {
}