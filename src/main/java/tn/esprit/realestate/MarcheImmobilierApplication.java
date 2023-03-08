package tn.esprit.realestate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MarcheImmobilierApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarcheImmobilierApplication.class, args);
    }

}
