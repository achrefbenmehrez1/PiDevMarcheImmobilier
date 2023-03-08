package tn.esprit.realestate.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.User;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {
    //public List<Advertisement> findByUser(User user);
    public Optional<Advertisement> findByForeignAdUrl(String foreignAdUrl);

    Page<Advertisement> findByUser(User user, Pageable pageable);
    Page<Advertisement> findAll(Specification<Advertisement> spec, Pageable pageable);
    Page<Advertisement> findAll(Pageable pageable);

    Page<Advertisement> findByScrapedFalse(Pageable pageable);

    List<Advertisement> findByScrapedFalse();

    Page<Advertisement> findByScrapedFalse(Specification<Advertisement> spec, Pageable pageable);


}