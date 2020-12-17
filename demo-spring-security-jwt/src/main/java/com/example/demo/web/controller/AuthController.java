package com.example.demo.web.controller;

import com.example.demo.data.dto.LoginRequest;
import com.example.demo.web.security.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService jwtUserDetailService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserDetailsService jwtUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.jwtUserDetailService = jwtUserDetailService;
    }

    @PostMapping("/auth")
    public String addAuth(@RequestBody LoginRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getAccount(), req.getPassword()));
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(req.getAccount());
        return jwtTokenService.generate(userDetails.getUsername());
    }
}
