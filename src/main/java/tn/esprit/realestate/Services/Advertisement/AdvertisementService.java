package tn.esprit.realestate.Services.Advertisement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Property;
import tn.esprit.realestate.Entities.User;
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
    public void addAdvertisement(Advertisement add, long userId) {
        Property prop = add.getProperty();
        User user=userRepository.findById(userId).get();
        add.setUser(user);
        propertyRepository.save(prop);
        advertisementRepository.save(add);
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
            ad.setUser(existingAd.getUser());
            advertisementRepository.save(ad);
        }
        return ad;
    }

    @Override
    public List<Advertisement> getAllAds() {
        return advertisementRepository.findAll();
    }


}
