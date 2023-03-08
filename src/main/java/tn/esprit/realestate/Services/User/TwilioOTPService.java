package tn.esprit.realestate.Services.User;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.realestate.Config.JwtService;
import tn.esprit.realestate.Config.TwilioConfig;
import tn.esprit.realestate.Dto.OtpStatus;
import tn.esprit.realestate.Dto.PasswordResetRequestDto;
import tn.esprit.realestate.Dto.PasswordResetResponseDto;
import reactor.core.publisher.Mono;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.UserRepository;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class TwilioOTPService {
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    Map<String, String> otpMap = new HashMap<>();

    public Mono<PasswordResetResponseDto> sendOTPForPasswordReset(PasswordResetRequestDto passwordResetRequestDto) {

        PasswordResetResponseDto passwordResetResponseDto = null;
        Optional<User> user = userRepository.findByUsername(passwordResetRequestDto.getUserName());
        String userphone= user.get().getPhone();


            if (user.isPresent() && userphone.equals(passwordResetRequestDto.getPhoneNumber())){
            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMessage = "Dear Customer , Your OTP is ##" + otp + "##. Use this Passcode to complete your transaction. Thank You.";
            Message message = Message
                    .creator(to, from,
                            otpMessage)
                    .create();
            otpMap.put(passwordResetRequestDto.getUserName(), otp);
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
            }else {
                passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.FAILED, "User not found");
            }

        return Mono.just(passwordResetResponseDto);
    }

    public Mono<String> validateOTP(String userInputOtp, String userName) {
        if (userInputOtp.equals(otpMap.get(userName))) {
            User user = userRepository.findByUsername(userName).get();
            String token = jwtService.generateToken(user);

            otpMap.remove(userName,userInputOtp);

            return Mono.just("Valid OTP :"+token);
        } else {
            return Mono.error(new IllegalArgumentException("Invalid otp please retry !"));
        }
    }

    //6 digit otp
    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

}
