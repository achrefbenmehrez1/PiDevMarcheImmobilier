package tn.esprit.realestate.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.IServices.IAdvertisementService;


import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdvertisementController {
    @Autowired
    IAdvertisementService advertisementService;

    @PostMapping("/addAd/{id}")
    public void addAdvertisement(@RequestBody Advertisement add,@PathVariable(value = "id") long userId){

        advertisementService.addAdvertisement(add,userId);
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


}
