package tn.esprit.realestate.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.realestate.Dto.OfferDto;
import tn.esprit.realestate.Entities.Details;
import tn.esprit.realestate.Entities.Offer;
import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Services.Offer.DetailsService;
import tn.esprit.realestate.Services.Offer.EmailSenderService;
import tn.esprit.realestate.Services.Offer.OfferService;
import tn.esprit.realestate.Services.Offer.PDFGeneratorService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;


    @Autowired
    private DetailsService detailsService;
    @Autowired
    private final PDFGeneratorService pdfGeneratorService;
    @Autowired
    private UserRepository userRepository;

    public OfferController(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping("/pdf/generate/{id}")
    public void generatePDF(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.export(id, response.getOutputStream());
    }

    @GetMapping("")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("{id}")
    public Offer getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id);
    }

    @PostMapping("addoffer/{id}")
    public Offer createOffer(@NonNull HttpServletRequest request, @RequestBody Offer offer,@PathVariable Long id) {

        return offerService.createOffer(request,offer,id);
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
        return detailsService.getDetailsById(offerId, id);
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
    public void deleteDetails(@PathVariable Long offerId, @PathVariable Long id) {
        detailsService.deleteDetails(offerId, id);
    }

    @PutMapping("participate/{idoffre}")
    public ResponseEntity<?> markOfferAsParticipate(@PathVariable Long idoffre) {
        offerService.markOfferAsParticipate(idoffre);
        return ResponseEntity.ok().build();
    }

    @GetMapping("findAlldto")
    public ResponseEntity<List<OfferDto>> retrieveAllOffresdto() {
        return ResponseEntity.ok(offerService.retrieveAllOffresdto());
    }
    @DeleteMapping("/remove-offre/{idoffre}/{id}")
    public void removeOffre(@PathVariable("idoffre") Long idoffre, @PathVariable("id") Long id) {
        offerService.removeOffre(idoffre,id);
    }
   /* @Autowired
    private ParticipateRepository participateRepository;*/
   /* @GetMapping("/{offerId}")
    public List<User> getClientsForOffer(@PathVariable Long offerId)
    {
        List<User> users = participateRepository.findByOfferId(offerId);
        return users;
    }*/


    /* @GetMapping("/offers/participate")
     public ResponseEntity<List<Offer>> getparticipateOffers() {
         List<Offer> interesseOffers = offerService.getparticipateOffers();
         return ResponseEntity.ok(interesseOffers);
     }*/
    @Autowired
    EmailSenderService emailSenderService;
    @PostMapping("/send_email/{id}")
    public String sendEmail(@PathVariable(value = "id") Long id) throws IOException {
        OutputStream pdf = new ByteArrayOutputStream();
        pdf.close();
        Role role=Role.PROMOTER;
        List<User> promoters = userRepository.findByRole(role);

        this.pdfGeneratorService.export(id, pdf);
        SimpleMailMessage msg = new SimpleMailMessage();

            for (User user : promoters) {
                String fromEmail = "realestatevendor2023@gmail.com";

                ByteArrayInputStream at=new ByteArrayInputStream(((ByteArrayOutputStream)pdf).toByteArray());
                emailSenderService.sendEmail(fromEmail, user.getEmail(), "New offer", "New offre is available", at);

            }
        return "Email sent successfully";
    }

}



