package com.halftusk.authentication.authservice.config;

import com.halftusk.authentication.authservice.security.filter.TokenAuthFilter;
import com.halftusk.authentication.authservice.security.filter.UsernamePasswordAuthFilter;
import com.halftusk.authentication.authservice.security.provider.OtpAuthenticationProvider;
import com.halftusk.authentication.authservice.security.provider.TokenAuthProvider;
import com.halftusk.authentication.authservice.security.provider.UsernamePasswordAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsernamePasswordAuthProvider authProvider;

    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Autowired
    private TokenAuthProvider tokenAuthProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }).and().requestMatchers()
                .antMatchers("**/registration")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();

        http.cors(c -> {
            CorsConfigurationSource cs = r -> {
                CorsConfiguration cc = new CorsConfiguration();
                cc.setAllowedOrigins(List.of("*"));
                cc.setAllowedMethods(List.of("GET","POST"));
                return cc;
            };

            c.configurationSource(cs);
        });
    }

    @Bean
    public UsernamePasswordAuthFilter usernamePasswordAuthFilter() {
        return new UsernamePasswordAuthFilter();
    }

    @Bean
    public TokenAuthFilter tokenAuthFilter() {
        return new TokenAuthFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider)
                .authenticationProvider(otpAuthenticationProvider)
                .authenticationProvider(tokenAuthProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
