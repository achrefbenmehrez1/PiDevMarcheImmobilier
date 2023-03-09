package tn.esprit.realestate.Services.Advertisement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tn.esprit.realestate.Dto.AdvertisementDto;
import tn.esprit.realestate.Entities.*;
import tn.esprit.realestate.IServices.IAdvertisementService;
import tn.esprit.realestate.IServices.IUserService;
import tn.esprit.realestate.Repositories.AdvertisementRepository;
import tn.esprit.realestate.Repositories.FavoriteAdRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;
import java.io.*;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@EnableScheduling
public class AdvertisementService implements IAdvertisementService {

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IUserService userService;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    FavoriteAdRepository favoriteAdRepository;





    public AdvertisementDto getAdById(long id){
         Advertisement ad= advertisementRepository.findById(id).get();
        return new AdvertisementDto().fromEntityToDTO(ad);

    }

    @Override
    public void addAd(String title,Double price, String description, TypeAd typeAd, Double size, Type type, int rooms, boolean parking, Double yardSpace,
                      boolean garage, String region,String ville, List<MultipartFile> photos, @NonNull HttpServletRequest request) throws IOException{



        Advertisement ad=new Advertisement(title,price,description,typeAd);

        List<String> photosPaths=storeImage(photos);
        Property prop=new Property(size, type, rooms, parking,yardSpace,garage,ville, region,photosPaths);

        User user = userService.getUserByToken(request);
        ad.setProperty(prop);
        ad.setUser(user);

        propertyRepository.save(prop);
        advertisementRepository.save(ad);

    }


/*
    public void addAd(AdvertisementDto advertisementDto, List<MultipartFile> photos, HttpServletRequest request) throws IOException{
        Advertisement ad = new Advertisement(advertisementDto.getTitle(), advertisementDto.getPrice(), advertisementDto.getDescription(), advertisementDto.getTypeAd());
        List<File> files = storeImage(photos);
        List<String> photoPaths = new ArrayList<>();
        for (File file : files) {
            String imagePath = file.getAbsolutePath();
            photoPaths.add(imagePath);
        }


        Property prop = new Property(advertisementDto.getSize(), advertisementDto.getType(), advertisementDto.getRooms(),
                advertisementDto.getParking(), advertisementDto.getYardSpace(), advertisementDto.getGarage(),
                advertisementDto.getVille(), advertisementDto.getRegion(), photoPaths);

        User user = userService.getUserByToken(request);
        //Advertisement ad = new Advertisement(advertisementDto.getTitle(), advertisementDto.getPrice(), advertisementDto.getDescription(), advertisementDto.getTypeAd());
        ad.setProperty(prop);
        ad.setUser(user);
        propertyRepository.save(prop);
        advertisementRepository.save(ad);

    }

*/


    public List<String> storeImage(List<MultipartFile> images) throws IOException{
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images){
            if (image != null && !image.isEmpty()){
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                //getting current directory
                String currentDir = System.getProperty("user.dir");
                Path uploadDir = Paths.get(currentDir, "src", "main", "resources", "images");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                try (InputStream inputStream = image.getInputStream()) {
                    Path filePath = uploadDir.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    String imagePath = filePath.toAbsolutePath().toString();
                    imagePaths.add(imagePath);
                } catch (IOException ex) {
                    throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
                }
            }
        }
        for (String img:imagePaths){
            System.out.println(img);
        }
        return imagePaths;
    }


    //test
    /*
    public String storeImage(MultipartFile profileImage) throws IOException {
        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
            //getting current directory
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

     */




    @Override
    public boolean deleteAdvertisement(long id,@NonNull HttpServletRequest request) {
        User user =userService.getUserByToken(request);

        Advertisement ad= advertisementRepository.findById(id).get();

        if(user.getId()==ad.getUser().getId()) {


            if (ad == null) {
                return false;
            } else {
                Property prop = ad.getProperty();
                advertisementRepository.delete(ad);
                propertyRepository.delete(prop);
                return true;
            }

        }else {
            // Return 403 Forbidden if the current user is not the owner of the advertisement
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this advertisement");
        }


    }

    // UPDATE :!!

    @Override
    public Advertisement updateAdvertisement(long id,String title,Double price, String description, TypeAd typeAd,
                                             Double size, Type type, Integer rooms, Boolean parking,
                                             Double yardSpace, Boolean garage, String region,
                                             String ville,List<MultipartFile> photo,@NonNull HttpServletRequest request) throws IOException {

        User user =userService.getUserByToken(request);
        Advertisement ad = advertisementRepository.findById(id).get();


        Advertisement existingAd = advertisementRepository.findById(ad.getId()).get();
        if(user.getId()!=existingAd.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this advertisement");
        }


        if (existingAd != null) {

            if (ad.getProperty() == null) {
                ad.setProperty(existingAd.getProperty());
            }
            if (ad.getUser() == null) {
                ad.setUser(existingAd.getUser());
            }

            if (title != null) {
                ad.setTitle(title);
            }

            if (price != null) {
                ad.setOldPrice(existingAd.getPrice());
                ad.setPrice(price);
            }

            if (description != null) {
                ad.setDescription(description);
            }
            if (typeAd != null) {
                ad.setTypeAd(typeAd);
            }
            if (size != null) {
                ad.getProperty().setSize(size);
            }

            if (type != null) {
                ad.getProperty().setType(type);
            }

            if (rooms != null) {
                ad.getProperty().setRooms(rooms);
            }

            if (yardSpace != null) {
                ad.getProperty().setYardSpace(yardSpace);
            }

            if (region != null) {
                ad.getProperty().setRegion(region);
            }
            if (garage != null) {
                ad.getProperty().setGarage(garage);
            }
            if (parking != null) {
                ad.getProperty().setParking(parking);
            }

            if (ville != null) {
                ad.getProperty().setVille(ville);
            }

            if (photo != null) {

                ad.getProperty().setPhoto(storeImage(photo));
            }

            propertyRepository.save(ad.getProperty());

            advertisementRepository.save(ad);

        }



        return ad;

    }





/*
    public AdvertisementDto updateAdvertisement(Long id,
                                                AdvertisementDto advertisementDto,
                                                @NonNull HttpServletRequest request,
                                                MultipartFile photo) throws IOException{
        User user = userService.getUserByToken(request);
        Advertisement existingAd= advertisementRepository.findById(id).get();
        if(user.getId()!=existingAd.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this advertisement");
        }

        if (advertisementDto.getTitle() != null) {
            existingAd.setTitle(advertisementDto.getTitle());
        }

        if (advertisementDto.getPrice() != null) {
            existingAd.setPrice(advertisementDto.getPrice());
        }

        if (advertisementDto.getDescription() != null) {
            existingAd.setDescription(advertisementDto.getDescription());
        }

        if (advertisementDto.getTypeAd() != null) {
            existingAd.setTypeAd(advertisementDto.getTypeAd());
        }

        if (advertisementDto.getProperty().getSize() != null) {
            existingAd.getProperty().setSize(advertisementDto.getProperty().getSize());
        }

        if (advertisementDto.getProperty().getType() != null) {
            existingAd.getProperty().setType(advertisementDto.getProperty().getType());
        }

        if (advertisementDto.getProperty().getRooms() != null) {
            existingAd.getProperty().setRooms(advertisementDto.getProperty().getRooms());
        }

        if (advertisementDto.getProperty().getYardSpace() != null) {
            existingAd.getProperty().setYardSpace(advertisementDto.getProperty().getYardSpace());
        }

        if (advertisementDto.getProperty().getRegion() != null) {
            existingAd.getProperty().setRegion(advertisementDto.getProperty().getRegion());
        }

        if (advertisementDto.getProperty().getGarage() != null) {
            existingAd.getProperty().setGarage(advertisementDto.getProperty().getGarage());
        }

        if (advertisementDto.getProperty().getParking() != null) {
            existingAd.getProperty().setParking(advertisementDto.getProperty().getParking());
        }

        if (advertisementDto.getProperty().getVille() != null) {
            existingAd.getProperty().setVille(advertisementDto.getProperty().getVille());
        }

        if (advertisementDto.getProperty().getPhoto() != null) {
            existingAd.getProperty().setPhoto(storeImage(photo));
        }

        propertyRepository.save(existingAd.getProperty());

        Advertisement updatedAd= advertisementRepository.save(existingAd);
        return new AdvertisementDto().fromEntityToDTO(updatedAd);

    }

 */




    /*
    @Override
    public List<Advertisement> getAllAds() {
        return advertisementRepository.findAll();
    }

     */

    /* get all ads withou DTO :
    @Override
    public Page<Advertisement> getAllAds(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return advertisementRepository.findAll(pageable);
    }

     */

    public Page<AdvertisementDto> getAllAds(int pageNumber, int pageSize, HttpServletRequest request){
        User user=userService.getUserByToken(request);
        if(!user.isPremium()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to see premuim ads");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Advertisement> advertisementPage = advertisementRepository.findAll(pageable);
        return advertisementPage.map(advertisement -> new AdvertisementDto().fromEntityToDTO(advertisement));
    }

    //Getting all ads for non-premium users
    @Override
    public Page<AdvertisementDto> getAdsNotPremium(int pageNumber, int pageSize) {
        Pageable pageable=PageRequest.of(pageNumber,pageSize);
        Page<Advertisement> advertisementPage=advertisementRepository.findByScrapedFalse(pageable);
        return advertisementPage.map(advertisement -> new AdvertisementDto().fromEntityToDTO(advertisement));
    }



    /* without DTO
    @Override
    public List<Advertisement> getUserAds(@NonNull HttpServletRequest request) {
        User user =userService.getUserByToken(request);
        return advertisementRepository.findByUser(user);
    }*/

    @Override
    public Page<AdvertisementDto> getUserAds(@NonNull HttpServletRequest request, int pageNumber, int pageSize){
        User user =userService.getUserByToken(request);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Advertisement> Userad = advertisementRepository.findByUser(user, pageable);
        return Userad.map(advertisement -> new AdvertisementDto().fromEntityToDTO(advertisement));
    }


    @Override
    public Page<AdvertisementDto> getAds(TypeAd typeAd, Type typeProp,
                                      String region,String ville,
                                      Integer rooms,
                                      Boolean parking, Boolean garage,
                                      Double maxPrice, Double minPrice,
                                      Double minSize, Double maxSize,
                                      int pageNumber, int pageSize,
                                         HttpServletRequest request  ) {

        User user=userService.getUserByToken(request);
        if(!user.isPremium()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to see premuim ads");
        }else {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<Advertisement> advertisements = advertisementRepository.findAll((Specification<Advertisement>) (root, cq, cb) -> {
                Predicate p = cb.conjunction();
                if (typeAd != null) {
                    p = cb.and(p, cb.equal(root.get("typeAd"), typeAd));
                }
                if (typeProp != null) {
                    p = cb.and(p, cb.equal(root.get("property").get("type"), typeProp));
                }

                if (region != null && !(region.isEmpty())) {
                    p = cb.and(p, cb.like(root.get("property").get("region"), "%" + region + "%"));
                }

                if (ville != null && !(ville.isEmpty())) {
                    p = cb.and(p, cb.like(root.get("property").get("ville"), "%" + ville + "%"));
                }

                if (rooms != null) {
                    p = cb.and(p, cb.equal(root.get("property").get("rooms"), rooms));
                }

                if (garage != null) {
                    p = cb.and(p, cb.equal(root.get("property").get("garage"), garage));
                }

                if (parking != null) {
                    p = cb.and(p, cb.equal(root.get("property").get("parking"), parking));
                }

                if (maxPrice != null) {
                    p = cb.and(p, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
                }

                if (minPrice != null) {
                    p = cb.and(p, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
                }

                if (minSize != null) {
                    p = cb.and(p, cb.greaterThanOrEqualTo(root.get("property").get("size"), minSize));
                }

                if (maxSize != null) {
                    p = cb.and(p, cb.lessThanOrEqualTo(root.get("property").get("size"), maxSize));
                }


                cq.orderBy(cb.asc(root.get("created_at")));
                return p;
            }, pageable);
            return advertisements.map(advertisement -> new AdvertisementDto()
                    .fromEntityToDTO(advertisement));
        }
    }


    public Page<AdvertisementDto> SearchNotScrapedAds(TypeAd typeAd, Type typeProp,
                                                      String region,String ville,
                                                      Integer rooms,
                                                      Boolean parking, Boolean garage,
                                                      Double maxPrice, Double minPrice,
                                                      Double minSize, Double maxSize,
                                                      int pageNumber, int pageSize){


        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Advertisement> advertisements = advertisementRepository.findAll((Specification<Advertisement>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();

            // Unscraped ADS
            p = cb.and(p, cb.equal(root.get("scraped"), false));

            if (typeAd != null) {
                p = cb.and(p, cb.equal(root.get("typeAd"), typeAd));
            }
            if (typeProp != null) {
                p = cb.and(p, cb.equal(root.get("property").get("type"), typeProp));
            }

            if (region != null && !(region.isEmpty())) {
                p = cb.and(p, cb.like(root.get("property").get("region"), "%" + region + "%"));
            }

            if (ville != null && !(ville.isEmpty())) {
                p = cb.and(p, cb.like(root.get("property").get("ville"), "%" + ville + "%"));
            }

            if (rooms != null) {
                p = cb.and(p, cb.equal(root.get("property").get("rooms"), rooms));
            }

            if (garage != null) {
                p = cb.and(p, cb.equal(root.get("property").get("garage"), garage));
            }

            if (parking != null) {
                p = cb.and(p, cb.equal(root.get("property").get("parking"), parking));
            }

            if (maxPrice != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (minPrice != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (minSize != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("property").get("size"), minSize));
            }

            if (maxSize != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("property").get("size"), maxSize));
            }

            cq.orderBy(cb.asc(root.get("created_at")));
            return p;
        }, pageable);
        return advertisements.map(advertisement -> new AdvertisementDto().fromEntityToDTO(advertisement));

    }


    public void addFavorite(HttpServletRequest request, Long id){

        User user =userService.getUserByToken(request);
        Advertisement ad=advertisementRepository.findById(id).get();
        if (!favoriteAdRepository.existsByUserAndAdvertisement(user, ad)) {
            FavoriteAd favorite = new FavoriteAd();
            favorite.setAdvertisement(ad);
            favorite.setUser(user);
            favoriteAdRepository.save(favorite);
        }


    }

    public List<AdvertisementDto> getUsersFavorites(HttpServletRequest request){
        User user = userService.getUserByToken(request);
        List<FavoriteAd> favoriteAds=favoriteAdRepository.findByUser(user);
        List<AdvertisementDto> ads = new ArrayList<>();
        for(FavoriteAd f:favoriteAds){
            Advertisement ad=f.getAdvertisement();
            ads.add(new AdvertisementDto().fromEntityToDTO(ad));
        }
        return ads;
    }

    public void removeFavorite(HttpServletRequest request, Long id){
        User user=userService.getUserByToken(request);
        Advertisement ad=advertisementRepository.findById(id).get();
        FavoriteAd favoriteAd=favoriteAdRepository.findByAdvertisementAndUser(ad,user);
        favoriteAdRepository.delete(favoriteAd);

    }

    public List<User> consultingFavorite(HttpServletRequest request, Long idAd){
        User user=userService.getUserByToken(request);
        Advertisement ad=advertisementRepository.findById(idAd).get();
        if(user.getId()!=ad.getUser().getId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to see the like of this advertisement");
        }
        List<User>users=new ArrayList<>();

        List<FavoriteAd> favorites= favoriteAdRepository.findByAdvertisement(ad);
        for(FavoriteAd f:favorites){
            users.add(f.getUser());
        }
        return users;

    }


/*

 oold !! without thymleaf

    @Override
    public ResponseEntity<?> contactAdOwner(long id, String message, HttpServletRequest request) {
        Advertisement ad = advertisementRepository.findById(id).get();

        //getting the sender's email
        User sender =userService.getUserByToken(request);
        String senderMail=sender.getEmail();

        //getting owner email
        String owner= ad.getUser().getEmail();
        System.out.println("sender email : "+senderMail+" owner email : "+owner);

        SimpleMailMessage msg= new SimpleMailMessage();
        try{

            String fromEmail= env.getProperty("spring.mail.username");

            msg.setFrom(fromEmail);
            msg.setSubject("Vendor.tn : A new contact request  regarding your property "+ad.getTitle());
            msg.setText(message);
            msg.setTo(owner);

            javaMailSender.send(msg);
            return ResponseEntity.ok("Email sent successfully");
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send email");
        }


    }

 */



    @Override
    public ResponseEntity<?> contactAdOwner(long id, String message, HttpServletRequest request) throws MessagingException {
        Advertisement ad = advertisementRepository.findById(id).get();

        //getting the sender's email
        User sender =userService.getUserByToken(request);
        String senderMail=sender.getEmail();

        //getting owner email
        String owner= ad.getUser().getEmail();
        System.out.println("sender email : "+senderMail+" owner email : "+owner);

        //creating email message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        try {
            //setting email parameters
            String fromEmail= env.getProperty("spring.mail.username");
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(owner);
            messageHelper.setSubject("Vendor.tn : A new contact request  regarding your property "+ad.getTitle());

            //creating context object for Thymeleaf
            Context thymeleafContext = new Context();


            thymeleafContext.setVariable("ownerName", ad.getUser().getUsername());
            thymeleafContext.setVariable("senderEmail", senderMail);
            thymeleafContext.setVariable("message", message);


            //using Thymeleaf to process email template with context object
            String htmlBody = templateEngine.process("email-template.html", thymeleafContext);
            messageHelper.setText(htmlBody, true);


            //Testing the directory :
            //System.out.println("Directory"+logoDirectory);

            //sending email
            javaMailSender.send(mimeMessage);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send email");
        }

    }







    @Scheduled(cron = "0 0 0 * * ?")
    public void getScrappedAds() throws IOException {

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

                        //Getting Ad Type
                        TypeAd typeAd;
                        if(url.contains("-a-vendre")){
                            typeAd=TypeAd.Sale;
                        }else{
                            typeAd=TypeAd.Rental;
                        }

                        //Getting prop Type
                        Type typeProp ;
                        if(url.contains("appartements")){
                            typeProp=Type.Appartment;
                        } else if (url.contains("maisons")) {
                            typeProp=Type.House;
                        } else if (url.contains("villas")) {
                            typeProp=Type.Villa;
                        } else if (url.contains("bureaux")) {
                            typeProp=Type.Office;
                        }else{
                            //if url.contains("terrains")
                            typeProp=Type.Land;
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

                        //getting the prop photo
                        String photoURL=annonce.select("img").attr("src");
                        List<String> photos=new ArrayList<>();
                        photos.add(photoURL);

                        // getting brief desription :

                        Element descriptionElement=annonce.selectFirst("p.listingP.descLi");
                        String description=null;
                        if (descriptionElement != null) {
                            descriptionElement.select("a").remove();
                             description = descriptionElement.text();
                        }

                        //scrape the details
                        Document propertyDoc = Jsoup.connect(propertyUrl).get();



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
                        boolean garage = false;

                        for (String word : words) {
                            if (word.toLowerCase().equals("garage")) {
                                garage=true;
                                break;
                            }
                        }



                        //Saving :

                        // Check if the advertisement already exists in the database:

                        Optional<Advertisement> optionalAd = advertisementRepository.findByForeignAdUrl(propertyUrl);
                        if (optionalAd.isPresent()) {
                            // The advertisement already exists in the database, skip it
                            continue;
                        }

                        Advertisement advertisement=new Advertisement(title,price,description,typeAd,propertyUrl,true);
                        Property property=new Property((double)surface,typeProp,chambres,garage,region,ville,photos);
                        propertyRepository.save(property);
                        advertisement.setProperty(property);
                        advertisementRepository.save(advertisement);



                        // scraping the next page
                        Element nextPageLink = mubawabDoc.getElementsByClass("Dots currentDot").first();
                        hasMorePages = nextPageLink != null;
                        page++;





                        System.out.println("Advertisement  : "+"Type Ad : "+typeAd+" Type Property : "+typeProp+" |Title : " + title + "  | price :" + price + " | propertyUrl : " + propertyUrl + " | Description : " + description +
                                " | old Text :" + attributeText + " | Surface : " + surface +
                                "  | chambres: " + chambres + " | pieces :" + pieces + " | Old region :" + regionText + " | ville: " + ville + " | region :" + region +
                                " | OldTextgarage :" + garageText + " | garage :" + garage+" |Photo: "+photoURL
                        );




                    }
                } else {
                    hasMorePages = false;
                }

            }
        }



    }

    @Override
    public Page<AdvertisementDto> getAdsByUsersLocation(HttpServletRequest request,int pageNumber, int pageSize) throws IOException {

        System.out.println(getUsersLocation(request).get(1));
        if(getUsersLocation(request).get(1).equals("TND")){
            return SearchNotScrapedAds(null, null,getUsersLocation(request).get(0),null,null,null,null,null,null,null,null,pageNumber,pageSize );

        }else{

            // Instantiates the OkHttpClient.
            OkHttpClient client = new OkHttpClient();
            List<Advertisement> ads=advertisementRepository.findByScrapedFalse();
            List<AdvertisementDto> adsDTO = new ArrayList<>();

             for (Advertisement ad:ads){

                String url ="https://api.apilayer.com/fixer/convert?to="
                        +getUsersLocation(request).get(1)+"&from=TND&amount="+ad.getPrice();


                Request requestApi = new Request.Builder()
                         .url(url)
                         .addHeader("apikey", "nbXr2diMnt83tIoIEf4SABmSBFL2d9bB")
                         .get().build();

                Response response = client.newCall(requestApi).execute();

                String api=  response.body().string();

                ObjectMapper objectMapper= new ObjectMapper();
                JsonNode rootNode=objectMapper.readTree(api);
                String msg=rootNode.path("result").asText();
                ad.setCurrency(getUsersLocation(request).get(1));
                ad.setPrice(Double.parseDouble(msg));
                adsDTO.add(new AdvertisementDto().fromEntityToDTO(ad));
             }

            return new PageImpl<>(adsDTO, PageRequest.of(pageNumber, pageSize), adsDTO.size());

        }

    }


    public List<String> getUsersLocation(HttpServletRequest request) throws IOException {

        String apiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=e2b0e27235ec45d0b1ec293f2212f512&ip=";
        /*
        String apiUrl = "http://api.ipapi.com/api/";
        String accessKey ="094eb0d06e5971764241ac33d3513d8a";

         */

        //String apiUrl="https://ipinfo.io/";
        //String accessKey="b2185636c4d8ae";

        String ipAddress = request.getHeader("X-Forwarded-For");
        //String ipAddress=getPublicIpAddress(request);



        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }


        // Check if IP address is IPv6 loopback address
        if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ipAddress = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }



        String apiUrlWithIpAddress = apiUrl + ipAddress;
        //String apiUrlWithIpAddress = apiUrl + ipAddress + "?token=" + accessKey;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrlWithIpAddress, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        String city = rootNode.path("city").asText();
        String currency=rootNode.path("currency").path("code").asText();

        //String region = rootNode.path("regionName").asText();

        List<String>list=new ArrayList<>();
        list.add(city);
        list.add(currency);
        return list;



    }


    // getting public IP address of the machine
    /*
    public String getPublicIpAddress(HttpServletRequest request) throws IOException {
        // getting public IP address of the machine
        URL url = new URL("https://api.ipify.org");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

     */







}
