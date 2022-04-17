package com.halftusk.authentication.authservice.security.provider;

import com.halftusk.authentication.authservice.security.authentications.OtpAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public static final String LOGIN_EMAIL = "login_email_";

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String otp = (String) authentication.getCredentials();
        String redisKey = LOGIN_EMAIL+username;
        String otpFromDB = String.valueOf(redisTemplate.opsForValue().get(redisKey));

        if (otpFromDB.equals(otp)) {
            return new OtpAuthentication(username, otp, List.of(() -> "USER"));
        }

        throw new BadCredentialsException("Please re-enter the correct otp");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OtpAuthentication.class.equals(aClass);
    }
}
