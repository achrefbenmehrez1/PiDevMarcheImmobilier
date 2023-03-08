package tn.esprit.realestate.IServices;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Entities.OfferParticipate;
import tn.esprit.realestate.Entities.User;

import java.io.IOException;
import java.util.List;

public interface IOfferService {
    public List<Offer> getAllOffers();
    public Offer getOfferById(Long id);
    public Offer createOffer(Offer offer);
    public Offer updateOffer(Long id, Offer updatedOffer);
    public void deleteOffer(Long id);
    public void markOfferAsParticipate(Long id);

    void removeOffreParticipate(Long id);

    OfferParticipate retrieveOffreParticipate(Long id);

    //void removeOffre(Long idoffre, Integer id);

    void removeOffre(Long idoffre, Long id);

    Offer retrieveOffre(Long idoffre);
    //public List<User> getClientsByOffer(Long id);
    //public List<Offer> getparticipateOffers();

}

