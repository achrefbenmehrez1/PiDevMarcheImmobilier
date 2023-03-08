package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import tn.esprit.realestate.Entities.OfferParticipate;


@Repository
public interface OfferParticipateRepository extends JpaRepository<OfferParticipate,Long> {
   /* @Query("select o from OfferParticipate o where o.offer.id = ?1 and o.offer.participate = true")
    List<User> findByOffer_User_IdAndOffer_ParticipateTrue(Long id);*/


}
