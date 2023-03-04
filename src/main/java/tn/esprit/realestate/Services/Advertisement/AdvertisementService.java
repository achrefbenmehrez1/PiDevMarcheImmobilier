package tn.esprit.realestate.Services.Advertisement;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            //Path uploadDir = Paths.get("C:/spring/PiDevMarcheImmobilier/src/main/resources/images");
            String currentDir = System.getProperty("user.dir");
            Path uploadDir = Paths.get(currentDir, "src", "main", "resources", "images");
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

    @Override
    public List<Advertisement> getScrappedAds() throws IOException {

        String [] urls={
        "appartements-a-louer","maisons-a-louer","villas-et-maisons-de-luxe-a-louer","bureaux-et-commerces-a-louer","terrains-a-louer",
        "appartements-a-vendre","maisons-a-vendre","villas-et-maisons-de-luxe-a-vendre","bureaux-et-commerces-a-vendre","terrains-a-vendre"
        };


        List<Advertisement> ads = new ArrayList<>();

        for (String url: urls) {
            System.out.println("--------"+url+"----------");
            int page = 1;
            boolean hasMorePages = true;
            String baseUrlMubaweb = "https://www.mubawab.tn/fr/sc/"+url;

            while (hasMorePages) {

                String urlMubaweb = page == 1 ? baseUrlMubaweb : baseUrlMubaweb + ":p:" + page;

                Document mubawabDoc = Jsoup.connect(urlMubaweb).get();

                Elements mubawabAnnonces = mubawabDoc.getElementsByClass("listingBox w100");
                System.out.println("page :" + page+" Size : "+mubawabAnnonces.size());
                if (!mubawabAnnonces.isEmpty()) {
                    for (Element annonce : mubawabAnnonces) {

                        //String TypeAd =mubawabDoc.getElementsByClass("btn btnFlatSmall active").text();

                        //Getting Ad Type
                        String typeAd;
                        if(url.contains("-a-vendre")){
                            typeAd="Sale";
                        }else{
                            typeAd="Rental";
                        }

                        //Getting prop Type
                        String typeProp = null;
                        if(url.contains("appartements")){
                            typeProp="Appartment";
                        } else if (url.contains("maisons")) {
                            typeProp="House";
                        } else if (url.contains("villas")) {
                            typeProp="Villa";
                        } else if (url.contains("bureaux")) {
                            typeProp="Office";
                        }else{
                            //if url.contains("terrains")
                            typeAd="Land";
                        }

                        String title = annonce.getElementsByTag("h2").text();


                        String priceText = annonce.select(".priceTag").text();
                        Double price = 0.0;

                        if (!priceText.isEmpty()) {
                            String digitsOnly = priceText.replaceAll("\\D", "");
                            if (!digitsOnly.isEmpty()) {
                                price = Double.parseDouble(digitsOnly);
                            }
                        }

                        // Getting the ad URL
                        String propertyUrl = annonce.select("a").attr("href");

                        //scrape the details
                        Document propertyDoc = Jsoup.connect(propertyUrl).get();

                        //getting description
                        String description = propertyDoc.getElementsByTag("p").text();

                        // getting the surface
                        String attributeText = propertyDoc.getElementsByClass("catNav ").text();

                        int surface = 0;
                        int pieces = 0;
                        int chambres = 0;

                        // Extract the surface
                        Pattern surfacePattern = Pattern.compile("(\\d+) m²");
                        Matcher surfaceMatcher = surfacePattern.matcher(attributeText);
                        if (surfaceMatcher.find()) {
                            surface = Integer.parseInt(surfaceMatcher.group(1));
                        }

                        // Extract the number of pieces
                        Pattern piecesPattern = Pattern.compile("(\\d+) Pièces");
                        Matcher piecesMatcher = piecesPattern.matcher(attributeText);
                        if (piecesMatcher.find()) {
                            pieces = Integer.parseInt(piecesMatcher.group(1));
                        }

                        // Extract the number of chambres
                        Pattern chambresPattern = Pattern.compile("(\\d+) Chambres");
                        Matcher chambresMatcher = chambresPattern.matcher(attributeText);
                        if (chambresMatcher.find()) {
                            chambres = Integer.parseInt(chambresMatcher.group(1));
                        }


                        // getting region
                        String regionText = propertyDoc.getElementsByClass("darkblue inBlock float-right floatL").text();
                        String[] wordsRegion = regionText.split(",");
                        String ville = wordsRegion[0];
                        String region = wordsRegion[1];


                        //getting garage

                        String garageText = propertyDoc.getElementsByClass("characIconText centered").text();
                        String[] words = garageText.split(" ");
                        String garage = null;

                        for (String word : words) {
                            if (word.toLowerCase().equals("garage")) {
                                garage = word;
                                break;
                            }
                        }

                        Element nextPageLink = mubawabDoc.getElementsByClass("Dots currentDot").first();
                        hasMorePages = nextPageLink != null;
                        page++;

                        //Saving :



                        System.out.println("Advertisement  : "+"Type Ad : "+typeAd+" Type Property : "+typeProp+" |Title : " + title + "  | price :" + price + " | propertyUrl : " + propertyUrl + " | Description : " + description +
                                " | old Text :" + attributeText + " | Surface : " + surface +
                                "  | chambres: " + chambres + " | pieces :" + pieces + " | Old region :" + regionText + " | ville: " + ville + " | region :" + region + " | OldTextgarage :" + garageText + " | garage :" + garage);

                    }
                } else {
                    hasMorePages = false;
                }

            }
        }

        return ads;

    }





}
