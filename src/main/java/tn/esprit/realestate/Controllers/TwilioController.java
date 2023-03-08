package tn.esprit.realestate.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tn.esprit.realestate.Dto.PasswordResetRequestDto;
import tn.esprit.realestate.Dto.PasswordResetResponseDto;
import tn.esprit.realestate.Services.User.TwilioOTPService;
@RequestMapping("/OTP")
@RestController
public class TwilioController {
    @Autowired
    private TwilioOTPService service;

    @PostMapping("/send")
    public Mono<PasswordResetResponseDto> sendOTP(@RequestBody PasswordResetRequestDto dto) {
        return service.sendOTPForPasswordReset(dto);
    }

    @PostMapping("/validate")
    public Object validateOTP(@RequestBody PasswordResetRequestDto dto) {
      return   service.validateOTP(dto.getOneTimePassword(), dto.getUserName());
       /* if (isValid!=null) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }*/
    }
}

