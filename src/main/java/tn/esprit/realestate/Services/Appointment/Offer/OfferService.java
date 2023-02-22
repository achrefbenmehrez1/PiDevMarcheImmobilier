package tn.esprit.realestate.Services.Appointment.Offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.IServices.IOfferService;
import tn.esprit.realestate.Repositories.OfferRepository;

import java.util.List;

@Service
public class OfferService implements  IOfferService{

    @Autowired
    private OfferRepository offerRepository;
    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public void addOffer(Offer add, long userId) {

    }


    @Override
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }
    @Override
    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }
    @Override
    public void updateOffer(Long id, Offer offer)  {
        Offer existingOffer = getOfferById(id);
        existingOffer.setDescription(offer.getDescription());
        existingOffer.setPrice(offer.getPrice());
        existingOffer.setProperty(offer.getProperty());
        existingOffer.setUser(offer.getUser());
        offerRepository.save(existingOffer);
    }
    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
