package com.web10.taskmanagement.controllers;

import com.web10.taskmanagement.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/test/token")
    public String getToken(@RequestParam String username) {
        UserDetails userDetails = User.withUsername(username)
                                      .password("password")
                                      .authorities("ROLE_USER")
                                      .build();
        return jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
    }
}
