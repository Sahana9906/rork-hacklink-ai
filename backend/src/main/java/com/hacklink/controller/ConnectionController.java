package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.ConnectionService;
import com.hacklink.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private final CurrentUserService currentUserService;
    private final ConnectionService connectionService;

    @PostMapping("/{userId}")
    Dtos.ConnectionResponse request(Authentication authentication, @PathVariable UUID userId) {
        return connectionService.request(currentUserService.require(authentication), userId);
    }

    @PostMapping("/{id}/accept")
    Dtos.ConnectionResponse accept(Authentication authentication, @PathVariable UUID id) {
        return connectionService.accept(currentUserService.require(authentication), id);
    }

    @PostMapping("/{id}/reject")
    Dtos.ConnectionResponse reject(Authentication authentication, @PathVariable UUID id) {
        return connectionService.reject(currentUserService.require(authentication), id);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(Authentication authentication, @PathVariable UUID id) {
        connectionService.delete(currentUserService.require(authentication), id);
        return ResponseEntity.noContent().build();
    }
}
