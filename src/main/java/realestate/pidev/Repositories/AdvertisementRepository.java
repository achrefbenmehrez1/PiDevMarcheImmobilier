package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
}