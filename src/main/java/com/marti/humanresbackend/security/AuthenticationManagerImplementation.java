package com.marti.humanresbackend.security;

import com.marti.humanresbackend.models.entities.User;
import com.marti.humanresbackend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class AuthenticationManagerImplementation implements AuthenticationManager {

    private final UserService userService;

    public AuthenticationManagerImplementation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        User u = userService.getUserByEmailOptional(authentication.getName());
        if(u != null && Objects.equals(u.getPass(), authentication.getCredentials().toString()) && u.getJobTitleId() != null)
        {
            log.info(authentication.getName() + "  successfully logged in!");
            return new UsernamePasswordAuthenticationToken(u.getId(), u.getPass(), Set.of(new SimpleGrantedAuthority(u.getRole().toString())));
        }
        throw new UsernameNotFoundException("Invalid credentials!");

    }
}
