package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Offer;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
}