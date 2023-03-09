package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}