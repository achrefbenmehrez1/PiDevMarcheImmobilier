package tn.esprit.realestate.IServices;

import tn.esprit.realestate.Entities.Details;

import java.util.List;

public interface IDetailsService {
        public List<Details> getAllDetails(Long offerId);
        public Details getDetailsById(Long offerId, Long id);
        public Details createDetails(Details details);
        public Details updateDetails(Long offerId, Long id, Details updatedDetails);

        public void deleteDetails(Long offerId, Long id);
}
