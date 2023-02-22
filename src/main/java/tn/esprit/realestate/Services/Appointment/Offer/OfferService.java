package tn.esprit.realestate.Services.Appointment.Offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.IOfferService;
import tn.esprit.realestate.Repositories.OfferRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.util.List;

@Service
public class OfferService implements  IOfferService{

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;
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
    public boolean createOffer(Offer offer, long userId, long propertyId) {
        User user = userRepository.findById(userId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        offer.setUser(user);
        offer.setProperty(property);
        offerRepository.save(offer);
        return true;
    }
    @Override
    public void updateOffer(Long id, Offer offer, long userId, long propertyId){
        Offer existingOffer = getOfferById(id);
        existingOffer.setDescription(offer.getDescription());
        existingOffer.setPrice(offer.getPrice());
        existingOffer.setProperty(propertyRepository.findById(propertyId).orElse(null));
        existingOffer.setUser(userRepository.findById(userId).orElse(null));
        offerRepository.save(existingOffer);
    }
    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
