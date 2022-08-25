package com.halftusk.authentication.authservice.controller;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.RegistrationRequest;
import com.halftusk.authentication.authservice.model.request.UserRegistrationConfirmationRequest;
import com.halftusk.authentication.authservice.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping
public class RegistrationController {

    @Autowired
    RegistrationService service;

    @PostMapping("/registration")
    public ResponseEntity<AppUser> userRegistration(@Valid @RequestBody RegistrationRequest registrationRequest){
        ResponseEntity<AppUser> responseEntity;
        try{
            AppUser appUser = service.userRegistration(registrationRequest);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(appUser);
        }catch (Exception exception){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return responseEntity;
    }

    @PostMapping("/confirm")
    public ResponseEntity<AppUser> confirmUserRegistration(@RequestBody UserRegistrationConfirmationRequest request){
        AppUser appUser = service.confirmUserRegistration(request);
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
