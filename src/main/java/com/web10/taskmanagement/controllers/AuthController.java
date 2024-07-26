package com.web10.taskmanagement.controllers;

import com.web10.taskmanagement.entity.User;
import com.web10.taskmanagement.dto.ErrorResponse;
import com.web10.taskmanagement.dto.JwtAuthenticationResponse;
import com.web10.taskmanagement.dto.LoginRequest;
import com.web10.taskmanagement.repository.UserRepository;
import com.web10.taskmanagement.security.JwtTokenProvider;
import com.web10.taskmanagement.services.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        try{if (userRepository.existsByUsername(user.getUsername())) {
            return errorResponse("Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return errorResponse("Email is already in use!");
        }

        User result = userService.registerUser(user);
        return ResponseEntity.ok(result);
    }catch (Exception e) {
        log.error("An error occurred while registering a user", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please contact support.");
    }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    private ResponseEntity<ErrorResponse> errorResponse(String message) {
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}