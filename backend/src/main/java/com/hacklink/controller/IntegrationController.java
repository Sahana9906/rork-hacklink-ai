package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.integration.GithubService;
import com.hacklink.integration.LinkedInService;
import com.hacklink.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integrations")
@RequiredArgsConstructor
public class IntegrationController {
    private final CurrentUserService currentUserService;
    private final GithubService githubService;
    private final LinkedInService linkedInService;

    @PostMapping("/github/connect")
    Dtos.GithubResponse connectGithub(Authentication authentication, @Valid @RequestBody Dtos.GithubConnectRequest request) {
        return githubService.connectUser(currentUserService.require(authentication), request);
    }

    @GetMapping("/github")
    Dtos.GithubResponse githubProfile(Authentication authentication) {
        return githubService.getUserProfile(currentUserService.require(authentication));
    }

    @PostMapping("/github/sync")
    Dtos.GithubResponse syncGithub(Authentication authentication) {
        return githubService.syncRepositories(currentUserService.require(authentication));
    }

    @DeleteMapping("/github")
    ResponseEntity<Void> disconnectGithub(Authentication authentication) {
        githubService.disconnectUser(currentUserService.require(authentication));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/linkedin/connect")
    ResponseEntity<Void> connectLinkedIn(Authentication authentication, @Valid @RequestBody Dtos.LinkedInConnectRequest request) {
        linkedInService.connect(currentUserService.require(authentication), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/linkedin")
    ResponseEntity<Void> disconnectLinkedIn(Authentication authentication) {
        linkedInService.disconnect(currentUserService.require(authentication));
        return ResponseEntity.noContent().build();
    }
}
