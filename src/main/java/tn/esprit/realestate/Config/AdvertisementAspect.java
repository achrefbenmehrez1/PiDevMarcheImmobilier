package tn.esprit.realestate.Config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tn.esprit.realestate.Entities.Advertisement;
import tn.esprit.realestate.Entities.FavoriteAd;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.IAdvertisementService;
import tn.esprit.realestate.Repositories.FavoriteAdRepository;
import tn.esprit.realestate.Services.User.UserService;

import java.util.List;

@Aspect
@Component
public class AdvertisementAspect {

    @Autowired
    UserService userService;

    @Autowired
    FavoriteAdRepository favoriteAdRepository;

    @Autowired
    IAdvertisementService advertisementService;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Autowired
    TemplateEngine templateEngine;

    @AfterReturning(pointcut = "execution(* tn.esprit.realestate.Services.Advertisement.AdvertisementService.updateAdvertisement(..))", returning = "result")
    public void sendEmailAfterAdvertisementUpdate(JoinPoint joinPoint, Object result) throws MessagingException {
        Advertisement updatedAd = (Advertisement) result;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //check if the ad has likes :
        boolean isFavorite = favoriteAdRepository.existsByAdvertisement(updatedAd);

        if (isFavorite && updatedAd.getPrice() != null) {
            List<User> users=advertisementService.consultingFavorite(request,updatedAd.getId());
           sendEmail(users, updatedAd);
        }
    }

    public void sendEmail(List<User> users,Advertisement updateAd) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


        //setting email parameters
        String fromEmail= env.getProperty("spring.mail.username");
        messageHelper.setFrom(fromEmail);
        for(User user:users) {
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Vendor.tn :  one of your favorite ads has");

            //creating context object for Thymeleaf
            Context thymeleafContext = new Context();

            thymeleafContext.setVariable("ownerName", user.getFirstname() + " " + user.getLastname());
            thymeleafContext.setVariable("ad_title", updateAd.getTitle());
            thymeleafContext.setVariable("old_price", updateAd.getOldPrice());
            thymeleafContext.setVariable("new_price", updateAd.getPrice());


            //using Thymeleaf to process email template with context object
            String htmlBody = templateEngine.process("email-template-Favorite.html", thymeleafContext);
            messageHelper.setText(htmlBody, true);



            //sending email
            javaMailSender.send(mimeMessage);
        }

    }

}
