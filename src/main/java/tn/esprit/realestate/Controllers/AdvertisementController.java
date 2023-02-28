package tn.esprit.realestate.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;
import tn.esprit.realestate.IServices.IAdvertisementService;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdvertisementController {
    @Autowired
    IAdvertisementService advertisementService;


    @PostMapping(value="/addAd/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
                      @RequestParam(value="photo",required = false) MultipartFile photo,
                      @PathVariable(value = "id")long userId) throws IOException {
        advertisementService.addAd(title,price,description,typeAd,size,type,
                rooms, parking,yardSpace,garage,region,photo,userId);
    }

    //test
    @PostMapping(value ="/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String storeProfileImage(@RequestParam MultipartFile profileImage) throws IOException {
        return profileImage.getOriginalFilename();
    }




    @PostMapping("/addAd/{id}")
    public Advertisement addAdvertisement(@RequestBody Advertisement add,@PathVariable(value = "id") long userId){

        return advertisementService.addAdvertisement(add,userId);
    }

    @DeleteMapping("/deleteAd/{id}")
    public boolean deleteAdvertisement(@PathVariable(value = "id") long id){
        return advertisementService.deleteAdvertisement(id);
    }

    @PutMapping("/updateAd")
    public Advertisement updateAdvertisement(@RequestBody Advertisement ad){

        return advertisementService.updateAdvertisement(ad);
    }

    @GetMapping("/getAds")
    public List<Advertisement> getAllAds(){
        return advertisementService.getAllAds();
    }

    @GetMapping("/getUsersAd/{id}")
    public List<Advertisement> getUserAds(@PathVariable(value = "id") long userid){
        return advertisementService.getUserAds(userid);
    }

    @GetMapping("/search")
    public List<Advertisement> getAds(@RequestParam(value="typeAd",required = false) TypeAd typeAd,
                                      @RequestParam(value="typeProp",required = false) Type typeProp,
                                      @RequestParam(value="region",required = false) String region,
                                      @RequestParam(value="rooms",required = false) Integer rooms)
    {
        return advertisementService.getAds(typeAd,typeProp,region,rooms);
    }


}
