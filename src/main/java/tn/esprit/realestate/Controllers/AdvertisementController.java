package tn.esprit.realestate.Controllers;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Dto.AdvertisementDto;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.IAdvertisementService;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdvertisementController {
    @Autowired
    IAdvertisementService advertisementService;


    @GetMapping("/getByid/{id}")
    public AdvertisementDto getAdById(@PathVariable(value = "id") long id){
        return  advertisementService.getAdById(id);
    }

// ADD WITHOUT DTO :

    @PostMapping(value="/addAd", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addAd(@RequestParam(value="title",required = false)String title,
                      @RequestParam(value="price",required = false)Double price,
                      @RequestParam(value="description",required = false)String description,
                      @RequestParam(value="typeAd",required = false)TypeAd typeAd,
                      @RequestParam(value="size",required = false)Double size,
                      @RequestParam(value="type",required = false)Type type,
                      @RequestParam(value="rooms",required = false)int rooms,
                      @RequestParam(value="parking",required = false)boolean parking,
                      @RequestParam(value="yardSpace",required = false) Double yardSpace,
                      @RequestParam(value="garage",required = false)boolean garage,
                      @RequestParam(value="region",required = false)String region,
                      @RequestParam(value="ville",required = false)String ville,
                      @RequestParam(value="photo",required = false) List<MultipartFile> photo,
                      @NonNull HttpServletRequest request
                     ) throws IOException {
        advertisementService.addAd(title,price,description,typeAd,size,type,
                rooms, parking,yardSpace,garage,region,ville,photo,request);
    }

     /*
    @PostMapping(value="/addAd", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addAd(@ModelAttribute AdvertisementDto advertisementDto,
                      @RequestParam(value="photo",required = false) List<MultipartFile> photos,
                      @NonNull HttpServletRequest request) throws IOException{
        advertisementService.addAd(advertisementDto,photos,request);
    }

      */






/*
    @PostMapping("/addAd/{id}")
    public Advertisement addAdvertisement(@RequestBody Advertisement add,@PathVariable(value = "id") long userId){

        return advertisementService.addAdvertisement(add,userId);
    }

 */

    @DeleteMapping("/deleteAd/{id}")
    public boolean deleteAdvertisement(@PathVariable(value = "id") long id,@NonNull HttpServletRequest request){
        return advertisementService.deleteAdvertisement(id,request);
    }

    @PutMapping("/updateAd/{id}")
    public Advertisement updateAdvertisement(@PathVariable(value = "id") long id,
                                             @RequestParam(value="title",required = false)String title,
                                             @RequestParam(value="price",required = false)Double price,
                                             @RequestParam(value="description",required = false)String description,
                                             @RequestParam(value="typeAd",required = false)TypeAd typeAd,
                                             @RequestParam(value="size",required = false)Double size,
                                             @RequestParam(value="type",required = false)Type type,
                                             @RequestParam(value="rooms",required = false)Integer rooms,
                                             @RequestParam(value="parking",required = false)Boolean parking,
                                             @RequestParam(value="yardSpace",required = false) Double yardSpace,
                                             @RequestParam(value="garage",required = false)Boolean garage,
                                             @RequestParam(value="region",required = false)String region,
                                             @RequestParam(value="ville",required = false)String ville,
                                             @RequestParam(value="photo",required = false) List<MultipartFile> photo,
                                             @NonNull HttpServletRequest request) throws IOException {

        return advertisementService.updateAdvertisement(id,title,price,description,typeAd,size,type,
                rooms, parking,yardSpace,garage,region,ville,photo, request);
    }






/*
    @PutMapping("/updateAd/{id}")
    public AdvertisementDto updateAdvertisement(@PathVariable(value = "id") Long id,
                                                @RequestPart(name = "advertisement") AdvertisementDto advertisementDTO,
                                                @NonNull HttpServletRequest request,
                                                @RequestParam(name = "photo", required = false) MultipartFile photo) throws IOException{
       return advertisementService.updateAdvertisement(id,advertisementDTO,request,photo);
    }

 */


    @GetMapping("/getAds")
    public Page<AdvertisementDto> getAllAds(@RequestParam(defaultValue = "0")int pageNumber,
                                            @RequestParam(defaultValue = "10") int pageSize){
        return advertisementService.getAllAds(pageNumber,pageSize);
    }
    @GetMapping("/getAdsNotPremium")
    public Page<AdvertisementDto> getAdsNotPremium(@RequestParam(defaultValue = "0")int pageNumber,
                                                   @RequestParam(defaultValue = "10")int pageSize){
        return  advertisementService.getAdsNotPremium(pageNumber,pageSize);
    }


    //DTO
    /*
    @GetMapping("/getAds")
    public Page<Advertisement> getAllAds(@RequestParam(defaultValue = "0")int pageNumber,@RequestParam(defaultValue = "10") int pageSize){
        return  advertisementService.getAllAds(pageNumber,pageSize);
    }

     */




    @GetMapping("/getUsersAd")
    public Page<AdvertisementDto> getUserAds(@NonNull HttpServletRequest request ,
                                          @RequestParam(defaultValue = "0")int pageNumber,
                                          @RequestParam(defaultValue = "10") int pageSize){
        return advertisementService.getUserAds(request,pageNumber,pageSize);
    }

    @GetMapping("/search")
    public Page<AdvertisementDto> getAds(@RequestParam(value="typeAd",required = false) TypeAd typeAd,
                                      @RequestParam(value="typeProp",required = false) Type typeProp,
                                      @RequestParam(value="region",required = false) String region,
                                      @RequestParam(value="ville",required = false)String ville,
                                      @RequestParam(value="rooms",required = false) Integer rooms,
                                      @RequestParam(value="parking",required = false) Boolean parking,
                                      @RequestParam(value="garage",required = false) Boolean garage,
                                      @RequestParam(value="maxPrice",required = false)Double maxPrice,
                                      @RequestParam(value="minPrice",required = false)Double minPrice,
                                      @RequestParam(value="minSize",required = false) Double minSize,
                                      @RequestParam(value="maxSize",required = false) Double maxSize,
                                      @RequestParam(defaultValue = "0")int pageNumber,
                                      @RequestParam(defaultValue = "10")int pageSize)
    {
        return advertisementService.getAds(typeAd,typeProp,region,ville,rooms,parking,garage,maxPrice,minPrice,
                minSize,maxSize,pageNumber,pageSize);
    }

    //Search not scraped ADS
    @GetMapping("/searchNotScraped")
    public Page<AdvertisementDto> SearchNotScrapedAds(@RequestParam(value="typeAd",required = false) TypeAd typeAd,
                                                      @RequestParam(value="typeProp",required = false) Type typeProp,
                                                      @RequestParam(value="region",required = false) String region,
                                                      @RequestParam(value="ville",required = false)String ville,
                                                      @RequestParam(value="rooms",required = false) Integer rooms,
                                                      @RequestParam(value="parking",required = false) Boolean parking,
                                                      @RequestParam(value="garage",required = false) Boolean garage,
                                                      @RequestParam(value="maxPrice",required = false)Double maxPrice,
                                                      @RequestParam(value="minPrice",required = false)Double minPrice,
                                                      @RequestParam(value="minSize",required = false) Double minSize,
                                                      @RequestParam(value="maxSize",required = false) Double maxSize,
                                                      @RequestParam(defaultValue = "0")int pageNumber,
                                                      @RequestParam(defaultValue = "10")int pageSize){
        return  advertisementService.SearchNotScrapedAds(typeAd,typeProp,region,ville,rooms,parking,garage,maxPrice,minPrice,
                minSize,maxSize,pageNumber,pageSize);
    }

    @GetMapping("/getAdsByUsersLocation")
    public Page<AdvertisementDto> getAdsByUsersLocation(HttpServletRequest request,
                                                     @RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "10") int pageSize) throws IOException{
        return advertisementService.getAdsByUsersLocation(request,pageNumber,pageSize);
    }

    @PostMapping("/{id}/contact")
    public ResponseEntity<?> contactAdOwner(@PathVariable(value = "id") long id,
                                            @RequestParam(value="message")String message,
                                            HttpServletRequest request) throws MessagingException {
        return advertisementService.contactAdOwner(id,message,request);
    }
    @PostMapping("/addFavorite/{id}")
    public void addFavorite(HttpServletRequest request, @PathVariable(value = "id") Long id){
        advertisementService.addFavorite(request,id);
    }

    @GetMapping("/favorites")
    public List<AdvertisementDto> getUsersFavorites(HttpServletRequest request){
        return advertisementService.getUsersFavorites(request);
    }

    @DeleteMapping("/removeFavorite/{id}")
    public void removeFavorite(HttpServletRequest request, @PathVariable(value = "id") Long id){
       advertisementService.removeFavorite(request,id);
    }

    @GetMapping("/getLikes/{id}")
    public List<User> consultingFavorite(HttpServletRequest request,  @PathVariable(value = "id") Long idAd){
        return advertisementService.consultingFavorite(request,idAd);
    }




/*
    @GetMapping("/ScrapedAds")
    public List<Advertisement> getScrappedAds() throws IOException{
        return advertisementService.getScrappedAds();
    }

 */




}
