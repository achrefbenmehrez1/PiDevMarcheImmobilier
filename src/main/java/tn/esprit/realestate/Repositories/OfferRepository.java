package tn.esprit.realestate.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Entities.User;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
   // List<Offer> findByinteresseTrue();
   // List<User> findByUserId();

   /*@Query("select o from User o where o.offer.id = ?1 and o.participate = true")
    List<User> findByUser_IdAndParticipateTrue(Long id);*/
}