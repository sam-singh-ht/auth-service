package com.halftusk.authentication.authservice.controller;

import com.halftusk.authentication.authservice.model.request.OtpVerificationRequest;
import com.halftusk.authentication.authservice.service.OtpVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OtpVerificationController {

    private final OtpVerificationService service;

    public OtpVerificationController(OtpVerificationService service) {
        this.service = service;
    }

    @PostMapping("/reset/verifyotp")
    public ResponseEntity<String> restPasswordVerification(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest){
        service.restPasswordVerification(otpVerificationRequest);
        return ResponseEntity.status(HttpStatus.OK).body("reset password is successful");
    }
}
