package tn.esprit.realestate.Services.Offer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;


@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String fromEmail,String toEMail,String subject,String body, InputStream attachement) {

            /*
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(toEMail);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailMessage.
            mailSender.send(mailMessage);
             */
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEMail);
            helper.setSubject(subject);
            helper.setText(body);

            if(attachement != null){
                ByteArrayDataSource attachment_bads = new ByteArrayDataSource(attachement, "application/octet-stream");
                helper.addAttachment("offer.pdf", attachment_bads);
            }
            /*
            FileSystemResource file = new FileSystemResource("C:\\log.txt");
            helper.addAttachment(file.getFilename(), file);
             */

            mailSender.send(message);

            System.out.println("Email Sent Successfully!!");
        } catch (MessagingException e) {
            System.out.println("Error sending email");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}