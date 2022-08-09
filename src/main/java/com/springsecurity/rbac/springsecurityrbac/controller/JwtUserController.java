package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.entity.JwtUserRequest;
import com.springsecurity.rbac.springsecurityrbac.entity.JwtUserResponse;
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

import java.time.LocalDateTime;
import java.util.Date;
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
    public ResponseEntity<JwtUserResponse> generateToken(@RequestBody JwtUserRequest jwtRequest) {
        JwtUtil jwtUtil = new JwtUtil();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getEmail());

        JwtUserResponse jwtUserResponse = new JwtUserResponse(
                jwtUtil.generateToken(user, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)),
                LocalDateTime.now().plusMinutes(10)
        );


        return new ResponseEntity<>(jwtUserResponse, HttpStatus.OK);
    }

}
