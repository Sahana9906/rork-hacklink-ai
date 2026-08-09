package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.CurrentUserService;
import com.hacklink.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;

    @GetMapping
    List<Dtos.NotificationResponse> list(Authentication authentication) {
        return notificationService.list(currentUserService.require(authentication));
    }

    @PostMapping("/{id}/read")
    ResponseEntity<Void> read(Authentication authentication, @PathVariable UUID id) {
        notificationService.markRead(currentUserService.require(authentication), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/read-all")
    ResponseEntity<Void> readAll(Authentication authentication) {
        notificationService.markAllRead(currentUserService.require(authentication));
        return ResponseEntity.noContent().build();
    }
}
