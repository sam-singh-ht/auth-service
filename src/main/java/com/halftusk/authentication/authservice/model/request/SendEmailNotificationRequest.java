package com.halftusk.authentication.authservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailNotificationRequest {
    private String emailId;
    private String otp;
    private boolean otpEnabled;
}
