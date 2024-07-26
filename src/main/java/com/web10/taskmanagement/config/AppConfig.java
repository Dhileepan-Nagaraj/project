package com.web10.taskmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.web10.taskmanagement.security.JwtTokenProvider;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Getter for jwtSecret
    public String getJwtSecret() {
        return jwtSecret;
    }

    public int getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
    
    // Other configurations and beans
}
