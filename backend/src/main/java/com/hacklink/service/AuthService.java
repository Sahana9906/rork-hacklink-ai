package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Profile;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.UserRepository;
import com.hacklink.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ApiMapper mapper;

    @Transactional
    public Dtos.AuthResponse register(Dtos.RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException("EMAIL_ALREADY_REGISTERED", "An account with this email already exists.", HttpStatus.CONFLICT);
        }
        User user = userRepository.save(new User(email, passwordEncoder.encode(request.password())));
        Profile profile = profileRepository.save(new Profile(user, request.fullName().trim()));
        user.setProfile(profile);
        UserDetails details = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash()).authorities("ROLE_USER").build();
        return new Dtos.AuthResponse(jwtService.generateToken(details), "Bearer", jwtService.expirationTime(), mapper.toProfile(profile));
    }

    @Transactional(readOnly = true)
    public Dtos.AuthResponse login(Dtos.LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
        User user = userRepository.findByEmailIgnoreCase(request.email()).orElseThrow(() -> new ApiException("USER_NOT_FOUND", "User was not found.", HttpStatus.NOT_FOUND));
        Profile profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ApiException("PROFILE_NOT_FOUND", "Profile was not found.", HttpStatus.NOT_FOUND));
        UserDetails details = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash()).authorities("ROLE_USER").build();
        return new Dtos.AuthResponse(jwtService.generateToken(details), "Bearer", jwtService.expirationTime(), mapper.toProfile(profile));
    }
}
