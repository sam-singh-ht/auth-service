package com.halftusk.authentication.authservice.security.filter;

import com.halftusk.authentication.authservice.entity.AppUser;
import com.halftusk.authentication.authservice.model.request.SendEmailNotificationRequest;
import com.halftusk.authentication.authservice.repository.AppUserRepository;
import com.halftusk.authentication.authservice.security.authentications.OtpAuthentication;
import com.halftusk.authentication.authservice.security.authentications.UsernamePasswordAuthentication;
import com.halftusk.authentication.authservice.service.EmailService;
import com.halftusk.authentication.authservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${app.email.login.otp.otpExpirationInHours}")
    private String otpExpirationInHours;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var username = request.getHeader("username");
        var password = request.getHeader("password");
        var otp = request.getHeader("otp");

        if (otp == null) {
            Authentication authentication = new UsernamePasswordAuthentication(username, password);
            authentication = authenticationManager.authenticate(authentication);
            String code = String.valueOf(new Random().nextInt(9999) + 1000);
            redisTemplate.opsForValue().set(username, code, Long.valueOf(otpExpirationInHours), TimeUnit.SECONDS);
            AppUser user = appUserRepository.findAppUserByUsername(username);
            SendEmailNotificationRequest emailNotificationRequest =
                    SendEmailNotificationRequest
                    .builder()
                    .emailId(user.getEmail())
                    .otp(code)
                    .otpEnabled(true)
                    .build();

            emailService.sendEmail(emailNotificationRequest);
        } else {
            Authentication authentication = new OtpAuthentication(username, otp);
            authentication = authenticationManager.authenticate(authentication);
            if(authentication.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            response.setHeader("Authorization", jwtUtils.generateJwtToken(authentication));
        }

        filterChain.doFilter(request, response);

    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        return !request.getServletPath().contains("/login");
    }
}
