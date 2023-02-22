package tn.esprit.realestate.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.Type;
import tn.esprit.realestate.Entities.TypeAd;
import tn.esprit.realestate.IServices.IAdvertisementService;


import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdvertisementController {
    @Autowired
    IAdvertisementService advertisementService;

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
                                      @RequestParam(value="region",required = false) String region)
    {
        return advertisementService.getAds(typeAd,typeProp,region);
    }


}
