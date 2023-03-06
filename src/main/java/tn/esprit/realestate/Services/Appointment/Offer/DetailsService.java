package tn.esprit.realestate.Services.Appointment.Offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Details;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.IServices.IDetailsService;
import tn.esprit.realestate.Repositories.DetailsRepository;
import tn.esprit.realestate.Repositories.OfferRepository;

import java.util.List;

@Service
public class DetailsService implements IDetailsService {

    @Autowired
    private DetailsRepository detailsRepository;

    @Override
    public List<Details> getAllDetails(Long offerId) {
        return detailsRepository.findByOffer_Id(offerId);
    }

    @Override
    public Details getDetailsById(Long offerId, Long id) {
        return detailsRepository.findByIdAndOffer_Id(offerId, id);
    }

    @Override
    public Details createDetails(Details details) {
        return detailsRepository.save(details);
    }

    @Override
    public Details updateDetails(Long offerId, Long id, Details updatedDetails) {
        Details existingDetails = getDetailsById(offerId, id);
        existingDetails.setPrice(updatedDetails.getPrice());
        existingDetails.setDeadline(updatedDetails.getDeadline());
        existingDetails.setDescription(updatedDetails.getDescription());
        return detailsRepository.save(existingDetails);
    }

    @Override
    public void deleteDetails(Long offerId, Long id) {
        Details existingDetails = getDetailsById(offerId, id);
        detailsRepository.delete(existingDetails);
   }
}