package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Details;
import tn.esprit.realestate.Entities.Offer;

import java.util.List;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long> {
    Details findByIdAndOffer_Id(Long id, Long id1);
    List<Details> findByOffer_Id(Long id);
}