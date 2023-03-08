package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Entities.OfferParticipate;
import tn.esprit.realestate.Entities.User;

import java.util.List;
@Repository
public interface ParticipateRepository extends JpaRepository<OfferParticipate,Long> {

   // @Query("SELECT c FROM OfferParticipate c WHERE c.offer.id = :offerId")
    //List<Client> findByOfferId(@Param("offerId") Long offerId);
    List<User> findByOfferId(Long offerId);


}
