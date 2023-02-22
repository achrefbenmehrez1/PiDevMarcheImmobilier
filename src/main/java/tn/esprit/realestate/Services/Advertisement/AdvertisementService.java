package tn.esprit.realestate.Services.Advertisement;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.*;
import tn.esprit.realestate.IServices.IAdvertisementService;
import tn.esprit.realestate.Repositories.AdvertisementRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.util.List;

@Service
public class AdvertisementService implements IAdvertisementService {

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public Advertisement addAdvertisement(Advertisement add, long userId) {
        Property prop = add.getProperty();
        User user=userRepository.findById(userId).get();
        add.setUser(user);
        propertyRepository.save(prop);
        return advertisementRepository.save(add);
    }

    @Override
    public boolean deleteAdvertisement(long id) {
        Advertisement ad= advertisementRepository.findById(id).get();
        if(ad==null){
            return  false;
        }else{
            Property prop= ad.getProperty();
            advertisementRepository.delete(ad);
            propertyRepository.delete(prop);
            return true;
        }

    }

    @Override
    public Advertisement updateAdvertisement(Advertisement ad) {

        Advertisement existingAd = advertisementRepository.findById(ad.getId()).get();

        if(existingAd != null){

            if(ad.getProperty()==null){
                ad.setProperty(existingAd.getProperty());
            }
            propertyRepository.save(ad.getProperty());
            ad.setUser(existingAd.getUser());
            advertisementRepository.save(ad);
        }
        return ad;
    }

    @Override
    public List<Advertisement> getAllAds() {
        return advertisementRepository.findAll();
    }

    @Override
    public List<Advertisement> getUserAds(long userid) {
        User user = userRepository.findById(userid).get();
        return advertisementRepository.findByUser(user);
    }

    @Override
    public List<Advertisement> getAds(TypeAd typeAd, Type typeProp, String region) {

        List<Advertisement> advertisements=advertisementRepository.findAll((Specification<Advertisement>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if( typeAd!=null ){
                p= cb.and(p,cb.equal(root.get("typeAd"),typeAd));
            }
            if( typeProp!=null  ){
                p=cb.and(p,cb.equal(root.get("property").get("type"),typeProp));
            }

            if(region!=null && !(region.isEmpty())){
                p=cb.and(p,cb.like(root.get("property").get("region"),"%"+region+"%"));
            }
            cq.orderBy(cb.asc(root.get("id")));
            return p;
        });
        return advertisements;
    }


}
