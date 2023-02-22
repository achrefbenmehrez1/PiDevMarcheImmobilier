package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.User;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {
    public List<Advertisement> findByUser(User user);
}