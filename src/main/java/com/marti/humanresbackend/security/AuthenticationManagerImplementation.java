package com.marti.humanresbackend.security;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class AuthenticationManagerImplementation implements AuthenticationManager {

    private final UserService userService;

    public AuthenticationManagerImplementation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User u = userService.getUserByEmail(authentication.getName());
        if(Objects.equals(u.getPass(), authentication.getCredentials().toString()))
        {
            return new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPass(), Set.of(new SimpleGrantedAuthority(u.getRole().toString())));
        }
        throw new RuntimeException("Invalid credentials!");
    }
}
