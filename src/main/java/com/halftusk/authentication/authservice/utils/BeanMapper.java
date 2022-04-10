package com.halftusk.authentication.authservice.utils;

import com.halftusk.authentication.authservice.entity.AppRole;
import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BeanMapper {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    public AppUser map(RegistrationRequest request) {

        AppRole appRole = new AppRole("USER");
        return new AppUser(request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getEmail(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                true,
                false,
                false,
                false,
                request.getPhoneNumber(),
                request.getRole(),
                LocalDateTime.now());
    }
}
