package tn.esprit.realestate.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Accueil")
public class Accueil {
    @GetMapping()
    public String Accueil() {
        return "Bienvenue";
    }
    @GetMapping("/Erreur")
    public String Erreur() {
        return "Erreur";
    }

}
