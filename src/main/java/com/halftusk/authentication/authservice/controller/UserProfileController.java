package com.halftusk.authentication.authservice.controller;

import com.halftusk.authentication.authservice.model.request.UserProfileRequest;
import com.halftusk.authentication.authservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody UserProfileRequest userProfileRequest){
        userProfileService.resetPassword(userProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password is updated successfully");
    }
}
