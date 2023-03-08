package tn.esprit.realestate.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.FavoriteAd;
import tn.esprit.realestate.Entities.User;

import java.util.List;

@Repository
public interface FavoriteAdRepository extends JpaRepository<FavoriteAd, Long> {

   public boolean existsByUserAndAdvertisement(User user, Advertisement advertisement);

   public List<FavoriteAd> findByUser(User user);

   public FavoriteAd findByAdvertisementAndUser(Advertisement advertisement, User user );

   public List<FavoriteAd> findByAdvertisement(Advertisement advertisement);

   public boolean existsByAdvertisement(Advertisement advertisement);


}
