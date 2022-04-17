package com.halftusk.authentication.authservice.service;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.OtpVerificationRequest;
import com.halftusk.authentication.authservice.model.request.UserProfileRequest;
import com.halftusk.authentication.authservice.repository.AppUserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OtpVerificationService {

    public static final String OTP_VERIFICATION = "FORGOT_PASSWORD_EMAIL_OTP_VERIFICATION_";
    private final AppUserRepository appUserRepository;

    private final RedisTemplate redisTemplate;

    private final PasswordEncoder bCryptPasswordEncoder;

    public OtpVerificationService(AppUserRepository appUserRepository, RedisTemplate redisTemplate,
                                  PasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.redisTemplate = redisTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void restPasswordVerification(OtpVerificationRequest otpVerificationRequest) {
        AppUser appUserByUsername = appUserRepository.findAppUserByUsername(otpVerificationRequest.getUsername());
        if(appUserByUsername == null){
            throw new RuntimeException("Username does not exist");
        }

        String redisKey = OTP_VERIFICATION + otpVerificationRequest.getUsername();

        UserProfileRequest otpObjectInRedis = (UserProfileRequest)redisTemplate.opsForValue().get(redisKey);
        if(otpObjectInRedis == null){
            throw new RuntimeException("OTP has been expired");
        }

        if(otpVerificationRequest!= null && otpVerificationRequest.getEmailOtp().equals(otpObjectInRedis.getOtp())){
            appUserByUsername.setPassword(bCryptPasswordEncoder.encode(otpObjectInRedis.getNewPassword()));
            appUserRepository.saveAndFlush(appUserByUsername);
        }
    }
}
