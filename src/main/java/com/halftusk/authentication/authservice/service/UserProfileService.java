package com.halftusk.authentication.authservice.service;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.SendEmailNotificationRequest;
import com.halftusk.authentication.authservice.model.request.UserProfileRequest;
import com.halftusk.authentication.authservice.repository.AppUserRepository;
import com.halftusk.authentication.authservice.utils.OtpGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UserProfileService {

    private final AppUserRepository appUserRepository;

    private final RedisTemplate redisTemplate;

    public final EmailService emailService;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Value("${app.email.resetPassword.otpExpirationInMin}")
    private String otpExpirationInMin;

    public static final String OTP_VERIFICATION = "FORGOT_PASSWORD_EMAIL_OTP_VERIFICATION_";

    public UserProfileService(AppUserRepository appUserRepository,
                              RedisTemplate redisTemplate,
                              EmailService emailService,
                              PasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AppUser resetPassword(UserProfileRequest userProfileRequest) {
        log.info("In resetPassword of UserProfileService");
        log.info("User id:: {}", userProfileRequest.getUsername());
        if(!userProfileRequest.getNewPassword().equals(userProfileRequest.getConfirmPassword())){
            throw new RuntimeException("New password and verify password does not match.");
        }
        AppUser appUserByUsername = appUserRepository.findAppUserByUsername(userProfileRequest.getUsername());
        if(appUserByUsername == null){
            throw new RuntimeException("Username does not exist");
        }

        if(!bCryptPasswordEncoder.matches(userProfileRequest.getOldPassword(), appUserByUsername.getPassword())){
            throw new RuntimeException("Your old password is incorrect");
        }

        String otp = OtpGenerator.generateOtp();
        userProfileRequest.setOtp(otp);
        String redisKey = OTP_VERIFICATION+userProfileRequest.getUsername();
        redisTemplate.opsForValue().set(redisKey, userProfileRequest, Long.valueOf(otpExpirationInMin), TimeUnit.MINUTES);

        boolean sendEmail = emailService.sendEmail(createEmailNotificationForResetPassword(appUserByUsername, otp));
        if(!sendEmail){
            throw new RuntimeException("Email is not sent successfully");
        }
        return appUserByUsername;
    }

    private SendEmailNotificationRequest createEmailNotificationForResetPassword(AppUser request, String otp) {

        return SendEmailNotificationRequest.builder()
                .emailId(request.getEmail())
                .otp(otp)
                .otpEnabled(false)
                .build();
    }
}
