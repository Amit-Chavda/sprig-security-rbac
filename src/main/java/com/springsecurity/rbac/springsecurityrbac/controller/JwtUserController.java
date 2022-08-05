package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.entity.JwtUserRequest;
import com.springsecurity.rbac.springsecurityrbac.service.UserDetailsServiceImpl;
import com.springsecurity.rbac.springsecurityrbac.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtUserController {

    private UserDetailsServiceImpl userDetailsService;
    private AuthenticationManager authenticationManager;

    public JwtUserController(UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/token")
    public String generateToken(@ModelAttribute JwtUserRequest jwtRequest) {
        JwtUtil jwtUtil = new JwtUtil();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword())
        );
        UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        return jwtUtil.generateToken(user);
    }

}
