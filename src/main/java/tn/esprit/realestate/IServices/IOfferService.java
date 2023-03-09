package tn.esprit.realestate.IServices;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Entities.OfferParticipate;

import java.util.List;

public interface IOfferService {
    public List<Offer> getAllOffers();
    public Offer getOfferById(Long id);
    public Offer createOffer(@NonNull HttpServletRequest request, Offer offer, Long id);
    public Offer updateOffer(Long id, Offer updatedOffer);
    public void deleteOffer(Long id);
    public void markOfferAsParticipate(Long id);

    void removeOffreParticipate(Long id);

    public OfferParticipate retrieveOffreParticipate(Long id);

    //void removeOffre(Long idoffre, Integer id);

    void removeOffre(Long idoffre, Long id);

    Offer retrieveOffre(Long idoffre);
    //public List<User> getClientsByOffer(Long id);
    //public List<Offer> getparticipateOffers();

}