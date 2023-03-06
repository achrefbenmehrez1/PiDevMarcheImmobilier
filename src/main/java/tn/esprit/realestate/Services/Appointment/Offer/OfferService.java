package tn.esprit.realestate.Services.Appointment.Offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Details;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.IServices.IOfferService;
import tn.esprit.realestate.Repositories.DetailsRepository;
import tn.esprit.realestate.Repositories.OfferRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.util.List;

@Service
public class OfferService implements IOfferService {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private DetailsRepository detailsRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).get();
    }

    @Override
    public Offer createOffer(Offer offer) {
        Details details = offer.getDetails();
        offer.setDetails(details);
        offer.setProperty(propertyRepository.findById(offer.getProperty().getId()).get());
        offer.setUser(userRepository.findById(offer.getUser().getId()).get());
        offerRepository.save(offer);

        details.setOffer(offer);
        detailsRepository.save(details);

        return offer;
    }

    @Override
    public Offer updateOffer(Long id, Offer updatedOffer) {
        Offer existingOffer = getOfferById(id);
        existingOffer.setDescription(updatedOffer.getDescription());
        existingOffer.setProperty(updatedOffer.getProperty());
        existingOffer.setUser(updatedOffer.getUser());
        Details details = detailsRepository.findById(existingOffer.getDetails().getId()).get();
        details.setPrice(updatedOffer.getDetails().getPrice());
        details.setDeadline(updatedOffer.getDetails().getDeadline());
        details.setPrice(updatedOffer.getDetails().getPrice());
        details.setDescription(updatedOffer.getDescription());
        details.setOffer(existingOffer);
        existingOffer.setDetails(updatedOffer.getDetails());
        return offerRepository.save(existingOffer);
    }

    public void deleteOffer(Long id) {
        Offer existingOffer = getOfferById(id);
        offerRepository.delete(existingOffer);
    }

}