package tn.esprit.realestate.Dto;

import lombok.Data;

@Data
public class PasswordResetRequestDto {
    private String phoneNumber;//destination
    private String userName;
    private String oneTimePassword;
}