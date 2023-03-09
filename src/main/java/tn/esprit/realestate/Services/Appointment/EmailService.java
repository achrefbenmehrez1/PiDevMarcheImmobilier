package tn.esprit.realestate.Services.Appointment;

import javax.mail.MessagingException;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMeetingLink(String to, String subject, String meetingLink) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Dear Client,\n" +
                "\n" +
                "I hope this email finds you well. I am writing to invite you to attend a meeting scheduled.Here's the link to the meeting: " + meetingLink);

        javaMailSender.send(message);
    }
}

