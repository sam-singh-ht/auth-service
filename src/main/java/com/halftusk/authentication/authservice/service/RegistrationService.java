package com.halftusk.authentication.authservice.service;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.RegistrationRequest;
import com.halftusk.authentication.authservice.model.request.SendEmailNotificationRequest;
import com.halftusk.authentication.authservice.model.request.UserRegistrationConfirmationRequest;
import com.halftusk.authentication.authservice.repository.AppUserRepository;
import com.halftusk.authentication.authservice.utils.BeanMapper;
import com.halftusk.authentication.authservice.utils.OtpGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RegistrationService {

    public static final String REGISTRATION = "Registration_";
    @Autowired
    BeanMapper beanToEntityMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    AppUserRepository repository;

    @Value("${app.email.registration.otpExpirationInHours}")
    private String otpExpirationInHours;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public AppUser userRegistration(RegistrationRequest registrationRequest) {
        boolean existsByUsername = repository.existsByUsername(registrationRequest.getUsername());
        boolean existsByEmail = repository.existsByEmail(registrationRequest.getEmail());
        boolean existsByPhoneNumber = repository.existsByPhoneNumber(registrationRequest.getPhoneNumber());
        log.info("is userName exist: {} or email id exist: {} or phone-number exist:{} ", existsByUsername, existsByEmail, existsByPhoneNumber);
        if(existsByEmail ){
            log.error("Email id already exist");
            throw new BadCredentialsException("Email id already exist");
        }

        if(existsByUsername){
            log.error("Username already exist");
            throw new BadCredentialsException("Username already exist");
        }

        if(existsByPhoneNumber){
            log.error("PhoneNumber already exist");
            throw new BadCredentialsException("PhoneNumber already exist");
        }

        AppUser user = beanToEntityMapper.map(registrationRequest);
        AppUser appUser = repository.save(user);
        SendEmailNotificationRequest emailNotificationRequest = createEmailNotificationRequest(registrationRequest);
        emailService.sendEmail(emailNotificationRequest);
        redisTemplate.opsForValue().set( REGISTRATION +registrationRequest.getUsername(), emailNotificationRequest.getOtp(), Long.valueOf(otpExpirationInHours), TimeUnit.HOURS);
        return appUser;
    }

    private SendEmailNotificationRequest createEmailNotificationRequest(RegistrationRequest request) {
        String otp = OtpGenerator.generateOtp();
        log.info("Random generated otp ", otp);
        return SendEmailNotificationRequest.builder()
                .emailId(request.getEmail())
                .otp(otp)
                .otpEnabled(false)
                .build();
    }

    public AppUser confirmUserRegistration(UserRegistrationConfirmationRequest request) {
        String otpFromRedis = String.valueOf(redisTemplate.opsForValue().get(REGISTRATION+request.getUsername()));
        if(request.getEmailOtp().equals(otpFromRedis)){
            AppUser appUser = repository.findAppUserByUsername(request.getUsername());
            if(appUser.isActive()){
                throw new BadCredentialsException("User is already registered");
            }
            appUser.setActive(true);
            repository.saveAndFlush(appUser);
            return appUser;
        }
        return null;
    }
}
