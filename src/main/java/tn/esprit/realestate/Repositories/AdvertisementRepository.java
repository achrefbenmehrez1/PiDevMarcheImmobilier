package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}