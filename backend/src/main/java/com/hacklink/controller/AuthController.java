package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<Dtos.AuthResponse> register(@Valid @RequestBody Dtos.RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    Dtos.AuthResponse login(@Valid @RequestBody Dtos.LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    Dtos.ProfileResponse me(org.springframework.security.core.Authentication authentication, com.hacklink.service.CurrentUserService currentUserService, com.hacklink.service.ProfileService profileService) {
        return profileService.get(currentUserService.require(authentication));
    }
}
