package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.CurrentUserService;
import com.hacklink.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final CurrentUserService currentUserService;
    private final InvitationService invitationService;

    @GetMapping
    List<Dtos.InvitationResponse> pending(Authentication authentication) {
        return invitationService.pending(currentUserService.require(authentication));
    }

    @PostMapping("/{id}/accept")
    Dtos.TeamResponse accept(Authentication authentication, @PathVariable UUID id) {
        return invitationService.accept(currentUserService.require(authentication), id);
    }

    @PostMapping("/{id}/reject")
    Dtos.InvitationResponse reject(Authentication authentication, @PathVariable UUID id) {
        return invitationService.reject(currentUserService.require(authentication), id);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> cancel(Authentication authentication, @PathVariable UUID id) {
        invitationService.cancel(currentUserService.require(authentication), id);
        return ResponseEntity.noContent().build();
    }
}
