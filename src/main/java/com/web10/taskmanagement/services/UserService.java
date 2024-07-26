package com.web10.taskmanagement.services;

import com.web10.taskmanagement.entity.Role;
import com.web10.taskmanagement.entity.User;
import com.web10.taskmanagement.repository.RoleRepository;
import com.web10.taskmanagement.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (existsByUsername(user.getUsername()) || existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        
        Role defaultRole = roleRepository.getDefaultRole();
        user.setRoles(new HashSet<>());
        user.getRoles().add(defaultRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.debug("Registering user with default role: {}", defaultRole.getName());
        return userRepository.save(user);
    }
    
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        if (StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be null, empty, or blank");
        }
        return userRepository.existsByEmail(email);
    }

    public User updateUser(User user) {
        if (!existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        User existingUser = findByUsernameOrEmail(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existingUser);
    }

    public void deleteUser(String username) {
        if (!existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        userRepository.deleteByUsername(username);
    }
}