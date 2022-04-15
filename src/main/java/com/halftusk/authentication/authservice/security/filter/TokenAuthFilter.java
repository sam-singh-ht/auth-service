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
        if(request.getServletPath().contains("/api/v1/registration")) {
            return true;
        }else if(request.getServletPath().contains("/api/v1/login")){
            return true;
        }else if(request.getServletPath().contains("/api/v1/confirm")){
            return true;
        }
        return false;
    }

}
