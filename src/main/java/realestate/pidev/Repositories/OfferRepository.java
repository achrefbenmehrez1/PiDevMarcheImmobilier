package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Offer;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
}