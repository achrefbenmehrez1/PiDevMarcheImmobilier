package tn.esprit.realestate.IServices;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import tn.esprit.realestate.Entities.Appointment;
import tn.esprit.realestate.Entities.Offer;

import java.io.IOException;
import java.util.List;

public interface IOfferService {
    public List<Offer> getAllOffers();
    public Offer getOfferById(Long id);
    public Offer createOffer(Offer offer);
    public Offer updateOffer(Long id, Offer updatedOffer);
    public void deleteOffer(Long id);

}

