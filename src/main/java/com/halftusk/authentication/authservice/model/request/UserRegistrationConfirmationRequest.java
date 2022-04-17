package com.halftusk.authentication.authservice.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationConfirmationRequest {
    @NotBlank(message = "Username can not be blank")
    private String username;
    @NotBlank(message = "emailOtp can not be blank")
    private String emailOtp;

    private String smsOtp;
}
