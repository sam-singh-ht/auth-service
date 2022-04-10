package com.halftusk.authentication.authservice.service;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.RegistrationRequest;
import com.halftusk.authentication.authservice.model.request.SendEmailNotificationRequest;
import com.halftusk.authentication.authservice.repository.AppUserRepository;
import com.halftusk.authentication.authservice.utils.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class RegistrationService {

    @Autowired
    BeanMapper beanToEntityMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    AppUserRepository repository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public AppUser userRegistration(RegistrationRequest registrationRequest) {
        boolean existsByUsername = repository.existsByUsername(registrationRequest.getUsername());
        boolean existsByEmail = repository.existsByEmail(registrationRequest.getEmail());
        boolean existsByPhoneNumber = repository.existsByPhoneNumber(registrationRequest.getPhoneNumber());
        log.info("is userName exist: {} or email id exist: {} or phone-number exist:{} ", existsByUsername, existsByEmail, existsByPhoneNumber);
        if(existsByEmail || existsByUsername || existsByPhoneNumber){
            log.error("Username or Email id or PhoneNumber already exist");
            throw new BadCredentialsException("Username or Email id or PhoneNumber already exist");
        }

        AppUser user = beanToEntityMapper.map(registrationRequest);
        AppUser appUser = repository.save(user);
        SendEmailNotificationRequest emailNotificationRequest = createEmailNotificationRequest(registrationRequest);
        emailService.sendEmail(emailNotificationRequest);
        redisTemplate.opsForValue().set( registrationRequest.getEmail(), emailNotificationRequest.getOtp());
        return appUser;
    }

    private SendEmailNotificationRequest createEmailNotificationRequest(RegistrationRequest request) {
        Random rnd = new Random();
        int number = rnd.nextInt(99999999);
        String otp = String.format("%08d", number);
        log.info("Random generated otp ", otp);
        return SendEmailNotificationRequest.builder().emailId(request.getEmail()).otp(otp).build();
    }

    public AppUser confirmUserRegistration(String emailId, String otp) {
        String otpFromRedis = String.valueOf(redisTemplate.opsForValue().get(emailId));
        if(otp.equals(otpFromRedis)){
            AppUser appUser = repository.findAppUserByEmail(emailId);
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
