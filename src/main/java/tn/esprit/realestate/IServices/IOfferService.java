package tn.esprit.realestate.IServices;

import org.springframework.data.crossstore.ChangeSetPersister;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Offer;

import java.util.List;

public interface IOfferService {
    List<Offer> getAllOffers();

    public void addOffer(Offer add, long userId);
    public Offer getOfferById(Long id) ;
    public Offer createOffer(Offer offer);
    public void updateOffer(Long id, Offer offer) ;
    public void deleteOffer(Long id);


}

