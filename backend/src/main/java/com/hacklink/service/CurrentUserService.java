package com.hacklink.service;

import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User require(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ApiException("UNAUTHENTICATED", "Authentication is required.", HttpStatus.UNAUTHORIZED);
        }
        return userRepository.findByEmailIgnoreCase(authentication.getName())
                .filter(User::isActive)
                .orElseThrow(() -> new ApiException("USER_NOT_FOUND", "The authenticated user no longer exists.", HttpStatus.UNAUTHORIZED));
    }

    public User requireById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("USER_NOT_FOUND", "User was not found.", HttpStatus.NOT_FOUND));
    }
}
