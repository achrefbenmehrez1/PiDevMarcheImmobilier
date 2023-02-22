package tn.esprit.realestate.Controllers;


import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.IServices.IOfferService;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {




    private IOfferService offerService;

    public OfferController(IOfferService offerService) {
        this.offerService = offerService;
    }


    @GetMapping("")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/{id}")
    public Offer getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id);
    }

    @PostMapping("/{userId}/{propertyId}")
    public boolean createOffer(@RequestBody Offer offer, @PathVariable Long userId, @PathVariable Long propertyId) {
        return offerService.createOffer(offer, userId, propertyId);
    }

    @PutMapping("/{id}/{userId}/{propertyId}")
    public void updateOffer(@PathVariable Long id, @RequestBody Offer offer, @PathVariable Long userId, @PathVariable Long propertyId) {
        offerService.updateOffer(id, offer, userId, propertyId);
    }

    @DeleteMapping("/{id}")
    public void deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
    }
}

