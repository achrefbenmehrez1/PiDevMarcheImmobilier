package tn.esprit.realestate.Controllers;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Details;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Services.Appointment.Offer.DetailsService;
import tn.esprit.realestate.Services.Appointment.Offer.OfferService;
import tn.esprit.realestate.Services.Appointment.Offer.PDFGeneratorService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private DetailsService detailsService;
    @Autowired
    private final PDFGeneratorService pdfGeneratorService;

    public OfferController(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }
   @GetMapping("/pdf/generate/{id}")
    public  void generatePDF(@PathVariable Long id ,HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime=dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.export(id,response);





    }

    @GetMapping("")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("{id}")
    public Offer getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id);
    }

    @PostMapping("")
    public Offer createOffer(@RequestBody Offer offer) {

        return offerService.createOffer(offer);
    }

    @PutMapping("{id}")
    public Offer updateOffer(@PathVariable Long id, @RequestBody Offer updatedOffer) {
        return offerService.updateOffer(id, updatedOffer);
    }

    @DeleteMapping("{id}")
    public void deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
    }

    @GetMapping("/{offerId}/details")
    public List<Details> getAllDetails(@PathVariable Long offerId) {
        return detailsService.getAllDetails(offerId);
    }

    @GetMapping("/{offerId}/details/{id}")
    public Details getDetailsById(@PathVariable Long offerId, @PathVariable Long id) {
        return detailsService.getDetailsById(offerId,id);
    }

    @PostMapping("/details")
    public Details createDetails(@RequestBody Details details) {
        return detailsService.createDetails(details);
    }

    @PutMapping("/details/{id}")
    public Details updateDetails(@PathVariable Long offerId, @PathVariable Long id, @RequestBody Details updatedDetails) {
        return detailsService.updateDetails(offerId, id, updatedDetails);
    }

    @DeleteMapping("/{offerId}/details/{id}")
    public void deleteDetails(@PathVariable Long offerId ,@PathVariable Long id) {
        detailsService.deleteDetails(offerId, id);
    }


}
