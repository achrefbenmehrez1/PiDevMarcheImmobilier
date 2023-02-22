package tn.esprit.realestate.IServices;

import org.springframework.data.crossstore.ChangeSetPersister;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Offer;

import java.util.List;

public interface IOfferService {
    List<Offer> getAllOffers();

    public void addOffer(Offer add, long userId);
    public Offer getOfferById(Long id) ;
    public boolean createOffer(Offer offer, long userId, long propertyId);
    public void updateOffer(Long id, Offer offer, long userId, long propertyId) ;
    public void deleteOffer(Long id);


}

