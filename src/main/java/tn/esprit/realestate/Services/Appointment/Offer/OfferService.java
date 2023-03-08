package tn.esprit.realestate.Services.Appointment.Offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.*;
import tn.esprit.realestate.IServices.IOfferService;
import tn.esprit.realestate.Repositories.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private OfferParticipateRepository offerParticipateRepository;


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

    @Override
    public void markOfferAsParticipate(Long id) {
        Offer offer = offerRepository.findById(id).get();
        OfferParticipate participate = new OfferParticipate();
        participate.setOffer(offer);
        participate.setDescription(offer.getDescription());
        participate.setUser(userRepository.findById(offer.getUser().getId()).get());
        offerParticipateRepository.save(participate);
        offer.setParticipate(true);
        offerRepository.save(offer);
    }

    private OfferDto mapToDTO(final Offer offer,
                              final OfferDto offerDTO) {
        offerDTO.setId(offer.getId());
        offerDTO.setParticipate(offer.getParticipate());
        offerDTO.setDescription(offer.getDescription());
//        offerDTO.setProperty(offer.getProperty());
//        offerDTO.setUser(offer.getUser());
//        offerDTO.setDetails(offer.getDetails());
       // offerDTO.setUser(userRepository.findById(offer.getUser().getId()).get());

        //offer.setUser(userRepository.findById(offer.getUser().getId()).get());


        //   offerDTO.setUserSell(sellerOffer.getUser() == null ? null : sellerOffer.getUser().getUserid());
        return offerDTO;
    }
    //getall
    public List<OfferDto> retrieveAllOffresdto() {
        final List<Offer> sellerOffers = offerRepository.findAll(Sort.by("id"));
        return sellerOffers.stream()
                .map((sellerOffer) -> mapToDTO(sellerOffer, new OfferDto()))
                .collect(Collectors.toList());
    }
    public List<OfferDto> getAllOffersdto() {
        final List<Offer> sellerOffers = offerRepository.findAll();
        return sellerOffers.stream()
                .map((sellerOffer) -> mapToDTO(sellerOffer, new OfferDto()))
                .collect(Collectors.toList());

      //  return offerRepository.findAll();
    }

    @Override
    public void removeOffreParticipate(Long id) {
        OfferParticipate o=retrieveOffreParticipate(id);
        offerParticipateRepository.delete(o);
    }


    @Override
    public OfferParticipate retrieveOffreParticipate(Long id) {

        return offerParticipateRepository.findById(id).get();

    }

    @Override
    public void removeOffre(Long idoffre,Long id) {
        Offer o=retrieveOffre(idoffre);
        OfferParticipate o1=retrieveOffreParticipate(id);
        offerRepository.delete(o);
        offerParticipateRepository.delete(o1);


    }
    @Override
    public Offer retrieveOffre(Long idoffre) {
        return offerRepository.findById(idoffre).get();

    }


   /* public List<User> getClientsByOffer(Long id) {
        return userRepository.findByOffreId(id);
    }*/



   /* @Override
    public List<Offer> getparticipateOffers() {
        return offerRepository.findByinteresseTrue();
    }*/



   /* @Autowired
    private OfferParticipateRepository participateRepository;

    //public List<User> getClientsForOffer(Long offerid) {


        List<User> participations = participateRepository.findByOffer_User_IdAndOffer_ParticipateTrue(offerid);

        return participations;
    }
*/

}