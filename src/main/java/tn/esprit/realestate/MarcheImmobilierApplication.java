package tn.esprit.realestate;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tn.esprit.realestate.Config.TwilioConfig;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MarcheImmobilierApplication {
    @Autowired
    private TwilioConfig twilioConfig;

    @PostConstruct
    public void initTwilio(){
        Twilio.init(twilioConfig.getAccountSid(),twilioConfig.getAuthToken());
    }
    public static void main(String[] args) {
        SpringApplication.run(MarcheImmobilierApplication.class, args);
    }

}
