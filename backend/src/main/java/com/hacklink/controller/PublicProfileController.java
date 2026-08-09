package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.PublicProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/profiles")
@RequiredArgsConstructor
public class PublicProfileController {
    private final PublicProfileService publicProfileService;

    @GetMapping("/{publicProfileId}")
    Dtos.PublicProfileResponse get(@PathVariable UUID publicProfileId) {
        return publicProfileService.get(publicProfileId);
    }
}
