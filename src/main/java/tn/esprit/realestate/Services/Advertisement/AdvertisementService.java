package tn.esprit.realestate.Services.Advertisement;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.*;
import tn.esprit.realestate.IServices.IAdvertisementService;
import tn.esprit.realestate.Repositories.AdvertisementRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public void addAd(String title,Double price, String description, TypeAd typeAd, Double size, Type type, int rooms, boolean parking, Double yardSpace,
                      boolean garage, String region, MultipartFile photo, long userId) throws IOException{



        Advertisement ad=new Advertisement(title,price,description,typeAd);

        Property prop=new Property(size, type, rooms, parking,yardSpace,garage, region, storeImage(photo));

        User user = userRepository.findById(userId).get();
        ad.setProperty(prop);
        ad.setUser(user);

        propertyRepository.save(prop);
        advertisementRepository.save(ad);

    }

    //test
    public String storeImage(MultipartFile profileImage) throws IOException {
        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
            Path uploadDir = Paths.get("C:/spring/PiDevMarcheImmobilier/src/main/resources/images");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            try (InputStream inputStream = profileImage.getInputStream()) {
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = filePath.toAbsolutePath().toString();
            } catch (IOException ex) {
                throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        return imagePath;
    }



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
    public Advertisement updateAdvertisement(long id,String title,Double price, String description, TypeAd typeAd,
                                             Double size, Type type, Integer rooms, Boolean parking,
                                             Double yardSpace, Boolean garage, String region,MultipartFile photo) throws IOException {

        Advertisement ad = advertisementRepository.findById(id).get();


        Advertisement existingAd = advertisementRepository.findById(ad.getId()).get();


        if(existingAd != null){

            if(ad.getProperty()==null){
                ad.setProperty(existingAd.getProperty());
            }
            if(ad.getUser()==null){
                ad.setUser(existingAd.getUser());
            }

            if(title!=null){
                ad.setTitle(title);
            }

            if(price!=null){
                ad.setPrice(price);
            }

            if(description!=null){
                ad.setDescription(description);
            }
            if(typeAd!=null){
                ad.setTypeAd(typeAd);
            }
            if(size!=null){
                ad.getProperty().setSize(size);
            }

            if(type!=null){
                ad.getProperty().setType(type);

            }

            if(rooms!=null){
                ad.getProperty().setRooms(rooms);
            }

            if(yardSpace!=null){
                ad.getProperty().setYardSpace(yardSpace);
            }

            if(region!=null){
                ad.getProperty().setRegion(region);
            }
            if(garage!=null){
                ad.getProperty().setGarage(garage);
            }
            if(parking!=null){
                ad.getProperty().setParking(parking);
            }



            if(photo!=null){
                ad.getProperty().setPhoto(storeImage(photo));
            }

            propertyRepository.save(ad.getProperty());

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
    public List<Advertisement> getAds(TypeAd typeAd, Type typeProp,
                                      String region, Integer rooms,
                                      Boolean parking, Boolean garage,
                                      Double maxPrice, Double minPrice,
                                      Double minSize, Double maxSize) {

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

            if(rooms!=null ){
                p=cb.and(p,cb.equal(root.get("property").get("rooms"),rooms));
            }

            if(garage!=null){
                p=cb.and(p,cb.equal(root.get("property").get("garage"),garage));
            }

            if(parking!=null){
                p=cb.and(p,cb.equal(root.get("property").get("parking"),parking));
            }

            if(maxPrice!=null){
                p=cb.and(p,cb.lessThanOrEqualTo(root.get("price"),maxPrice));
            }

            if(minPrice!=null){
                p=cb.and(p,cb.greaterThanOrEqualTo(root.get("price"),minPrice));
            }

            if(minSize!=null){
                p=cb.and(p,cb.greaterThanOrEqualTo(root.get("property").get("size"),minSize));
            }

            if(maxSize!=null){
                p=cb.and(p,cb.lessThanOrEqualTo(root.get("property").get("size"),maxSize));
            }


            cq.orderBy(cb.asc(root.get("id")));
            return p;
        });
        return advertisements;
    }


}
