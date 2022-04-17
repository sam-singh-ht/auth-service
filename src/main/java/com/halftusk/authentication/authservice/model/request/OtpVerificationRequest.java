package com.halftusk.authentication.authservice.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OtpVerificationRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String emailOtp;

}
