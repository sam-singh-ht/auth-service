package com.halftusk.authentication.authservice.controller;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.RegistrationRequest;
import com.halftusk.authentication.authservice.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    @Autowired
    RegistrationService service;

    @PostMapping("/registration")
    public ResponseEntity<AppUser> userRegistration(@Valid @RequestBody RegistrationRequest registrationRequest){
        AppUser appUser = service.userRegistration(registrationRequest);
        ResponseEntity<AppUser> responseEntity;
        try{
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(appUser);
        }catch (Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return responseEntity;
    }

    @GetMapping("/confirm")
    public ResponseEntity<AppUser> confirmUserRegistration(@RequestParam(required = true) String otp,
                                                           @RequestParam(required = true) String emailId){
        AppUser appUser = service.confirmUserRegistration(emailId, otp);
        ResponseEntity<AppUser> responseEntity;
        try{
            if(appUser != null){
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(appUser);
            }else {
                responseEntity = ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(appUser);
            }
        }catch (Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return responseEntity;
    }
}
