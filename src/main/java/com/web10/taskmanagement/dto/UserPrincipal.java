package com.web10.taskmanagement.dto;

import com.web10.taskmanagement.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Long id; // Change Integer to Long if User ID is of type Long
    private String username;
    private String email;
    private String password;
    private Set<String> roles;

    public UserPrincipal(Long id, String username, String email, String password, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public static UserPrincipal create(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName())  // Assuming getName() returns role names like "ROLE_USER"
                .collect(Collectors.toSet());

        return new UserPrincipal(
                user.getId(), // Ensure this matches the type
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                roles
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() { // Change return type to Long
        return id;
    }

    public String getEmail() {
        return email;
    }
}
