package tn.esprit.realestate.Services.Advertisement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Value;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
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
import tn.esprit.realestate.Dto.PropertyDto;
import tn.esprit.realestate.Entities.*;
import tn.esprit.realestate.IServices.IAdvertisementService;
import tn.esprit.realestate.IServices.IUserService;
import tn.esprit.realestate.Repositories.AdvertisementRepository;
import tn.esprit.realestate.Repositories.FavoriteAdRepository;
import tn.esprit.realestate.Repositories.PropertyRepository;
import tn.esprit.realestate.Repositories.UserRepository;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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


/*
    @Override
    public Advertisement addAdvertisement(Advertisement add, long userId) {
        Property prop = add.getProperty();
        User user=userRepository.findById(userId).get();
        add.setUser(user);
        propertyRepository.save(prop);
        return advertisementRepository.save(add);
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

    public Page<AdvertisementDto> getAllAds(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Advertisement> advertisementPage = advertisementRepository.findAll(pageable);
        return advertisementPage.map(advertisement -> new AdvertisementDto().fromEntityToDTO(advertisement));
    }

    /*
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
                                      int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Advertisement> advertisements=advertisementRepository.findAll((Specification<Advertisement>) (root, cq, cb) -> {
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

            if(ville!=null && !(ville.isEmpty())){
                p=cb.and(p,cb.like(root.get("property").get("ville"),"%"+ville+"%"));
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
        }, pageable);
        return advertisements.map(advertisement -> new AdvertisementDto()
                .fromEntityToDTO(advertisement));
    }

    @Override
    public Page<AdvertisementDto> getAdsByUsersLocation(HttpServletRequest request,int pageNumber, int pageSize) throws IOException {
        Page<AdvertisementDto>locatedAds=getAds(null, null,getUsersLocation(request),null,null,null,null,null,null,null,null,pageNumber,pageSize );
        return locatedAds;

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
            //String currentDir = System.getProperty("user.dir");
            //String path=currentDir+"/src/main/resources/images";
            //String logoDirectory = path.replace("\\", "/");
            //thymeleafContext.setVariable("logoDirectory",logoDirectory);

            //String logoData="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";
            //thymeleafContext.setVariable("logoData",logoData);
            thymeleafContext.setVariable("ownerName", ad.getUser().getFirstname()+ " " + ad.getUser().getLastname());
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



    public String getUsersLocation(HttpServletRequest request) throws IOException {

        //ipgeo : String apiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=e2b0e27235ec45d0b1ec293f2212f512&ip=";

        String apiUrl = "http://api.ipapi.com/api/";
        String accessKey ="094eb0d06e5971764241ac33d3513d8a";

        //String ipAddress = request.getHeader("X-Forwarded-For");
        String ipAddress=getPublicIpAddress(request);


        /*
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

         */

        //String apiUrlWithIpAddress = apiUrl + ipAddress;
        String apiUrlWithIpAddress = apiUrl + ipAddress + "?access_key=" + accessKey;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrlWithIpAddress, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        String city = rootNode.path("city").asText();
        //String region = rootNode.path("regionName").asText();

        //return new String[]{region,city};

        return city;

    }


    // getting public IP address of the machine
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







}
