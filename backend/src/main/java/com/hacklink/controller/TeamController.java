package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.CurrentUserService;
import com.hacklink.service.InvitationService;
import com.hacklink.service.TeamFinalizationService;
import com.hacklink.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final CurrentUserService currentUserService;
    private final TeamService teamService;
    private final InvitationService invitationService;
    private final TeamFinalizationService finalizationService;

    @PostMapping
    ResponseEntity<Dtos.TeamResponse> create(Authentication authentication, @Valid @RequestBody Dtos.CreateTeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.create(currentUserService.require(authentication), request));
    }

    @GetMapping
    List<Dtos.TeamResponse> mine(Authentication authentication) {
        return teamService.ownedBy(currentUserService.require(authentication));
    }

    @GetMapping("/{id}")
    Dtos.TeamResponse get(Authentication authentication, @PathVariable UUID id) {
        return teamService.get(currentUserService.require(authentication), id);
    }

    @GetMapping("/{id}/skill-coverage")
    Dtos.SkillCoverageResponse coverage(Authentication authentication, @PathVariable UUID id) {
        return teamService.coverage(currentUserService.require(authentication), id);
    }

    @GetMapping("/{id}/recommendations")
    List<Dtos.TeamMatchResponse> recommendations(Authentication authentication, @PathVariable UUID id) {
        return teamService.recommendations(currentUserService.require(authentication), id);
    }

    @PostMapping("/{id}/invitations")
    Dtos.InvitationResponse invite(Authentication authentication, @PathVariable UUID id, @Valid @RequestBody Dtos.InvitationRequest request) {
        return invitationService.create(currentUserService.require(authentication), id, request);
    }

    @PostMapping("/{id}/finalize")
    Dtos.TeamResponse finalizeTeam(Authentication authentication, @PathVariable UUID id) {
        return finalizationService.finalizeTeam(currentUserService.require(authentication), id);
    }

    @DeleteMapping("/{id}/members/me")
    ResponseEntity<Void> leave(Authentication authentication, @PathVariable UUID id) {
        teamService.leave(currentUserService.require(authentication), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    ResponseEntity<Void> remove(Authentication authentication, @PathVariable UUID id, @PathVariable UUID userId) {
        teamService.remove(currentUserService.require(authentication), id, userId);
        return ResponseEntity.noContent().build();
    }
}
