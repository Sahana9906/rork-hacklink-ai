package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.CurrentUserService;
import com.hacklink.service.HackathonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hackathons")
@RequiredArgsConstructor
public class HackathonController {
    private final CurrentUserService currentUserService;
    private final HackathonService hackathonService;

    @GetMapping
    List<Dtos.HackathonResponse> list(Authentication authentication) {
        return hackathonService.list(currentUserService.require(authentication));
    }

    @GetMapping("/{id}")
    Dtos.HackathonResponse get(Authentication authentication, @PathVariable UUID id) {
        return hackathonService.get(currentUserService.require(authentication), id);
    }

    @GetMapping("/{id}/skills")
    List<Dtos.HackathonSkillResponse> skills(@PathVariable UUID id) {
        return hackathonService.skills(id);
    }

    @GetMapping("/{id}/match")
    Dtos.HackathonMatchResponse match(Authentication authentication, @PathVariable UUID id) {
        return hackathonService.match(currentUserService.require(authentication), id);
    }

    @PostMapping("/{id}/register")
    Dtos.HackathonResponse register(Authentication authentication, @PathVariable UUID id) {
        return hackathonService.register(currentUserService.require(authentication), id);
    }
}
