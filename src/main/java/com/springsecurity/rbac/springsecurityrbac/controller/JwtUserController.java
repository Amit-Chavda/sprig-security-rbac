package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.entity.JwtUserRequest;
import com.springsecurity.rbac.springsecurityrbac.service.UserDetailsServiceImpl;
import com.springsecurity.rbac.springsecurityrbac.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JwtUserController {

    private UserDetailsServiceImpl userDetailsService;
    private AuthenticationManager authenticationManager;

    public JwtUserController(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody JwtUserRequest jwtRequest) {
        JwtUtil jwtUtil = new JwtUtil();


        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(exception.toString(), HttpStatus.BAD_REQUEST);
        }

        UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        return new ResponseEntity<>(jwtUtil.generateToken(user), HttpStatus.OK);
    }

}
