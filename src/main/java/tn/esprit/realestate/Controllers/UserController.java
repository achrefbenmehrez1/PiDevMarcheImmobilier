package tn.esprit.realestate.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.realestate.Dto.AuthenticationRequest;
import tn.esprit.realestate.Dto.AuthenticationResponse;
import tn.esprit.realestate.Dto.RegisterRequest;
import tn.esprit.realestate.Services.User.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final AuthenticationService service;

    @Autowired
    public UserController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
