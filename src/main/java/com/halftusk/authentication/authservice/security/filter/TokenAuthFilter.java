package com.halftusk.authentication.authservice.security.filter;

import com.halftusk.authentication.authservice.security.authentications.TokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthFilter extends UsernamePasswordAuthFilter {

    public static final String REGISTRATION = "/registration";
    public static final String LOGIN = "/login";
    public static final String CONFIRM = "/confirm";
    public static final String FORGOT_PASSWORD = "/forgotPassword";
    public static final String VERIFY_OTP = "/reset/verifyotp";
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("In TokenAuthFilter .....");
        var token = request.getHeader("Authorization");

        Authentication authentication = new TokenAuthentication(token, null);

        var a = authenticationManager.authenticate(authentication);

        SecurityContextHolder.getContext().setAuthentication(a);
        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        if(request.getServletPath().contains(REGISTRATION) ||
                request.getServletPath().contains(LOGIN)||
                request.getServletPath().contains(CONFIRM) ||
                request.getServletPath().contains(FORGOT_PASSWORD) ||
                request.getServletPath().contains(VERIFY_OTP)
        ) {
            return true;
        }
        return false;
    }

}
