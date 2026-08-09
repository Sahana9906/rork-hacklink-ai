package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.CurrentUserService;
import com.hacklink.service.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/internships")
@RequiredArgsConstructor
public class InternshipController {
    private final CurrentUserService currentUserService;
    private final InternshipService internshipService;

    @GetMapping
    List<Dtos.InternshipResponse> list(Authentication authentication) {
        return internshipService.list(currentUserService.require(authentication));
    }

    @GetMapping("/{id}")
    Dtos.InternshipResponse get(Authentication authentication, @PathVariable UUID id) {
        return internshipService.get(currentUserService.require(authentication), id);
    }

    @GetMapping("/{id}/match")
    Dtos.InternshipMatchResponse match(Authentication authentication, @PathVariable UUID id) {
        return internshipService.match(currentUserService.require(authentication), id);
    }

    @PostMapping("/{id}/save")
    Dtos.InternshipResponse save(Authentication authentication, @PathVariable UUID id) {
        return internshipService.save(currentUserService.require(authentication), id);
    }

    @DeleteMapping("/{id}/save")
    ResponseEntity<Void> unsave(Authentication authentication, @PathVariable UUID id) {
        internshipService.unsave(currentUserService.require(authentication), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/apply")
    Dtos.ApplicationResponse apply(Authentication authentication, @PathVariable UUID id) {
        return internshipService.apply(currentUserService.require(authentication), id);
    }
}
