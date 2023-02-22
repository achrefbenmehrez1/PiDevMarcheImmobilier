package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.User;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    public List<Advertisement> findByUser(User user);
}