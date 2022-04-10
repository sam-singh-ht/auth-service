package com.halftusk.authentication.authservice.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendEmailNotificationRequest {
    private String emailId;
    private String otp;
}
