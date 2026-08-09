package com.hacklink.controller;

import com.hacklink.dto.Dtos;
import com.hacklink.service.CurrentUserService;
import com.hacklink.service.ProfileService;
import com.hacklink.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final CurrentUserService currentUserService;
    private final ProfileService profileService;
    private final ResumeService resumeService;

    @GetMapping
    Dtos.ProfileResponse get(Authentication authentication) {
        return profileService.get(currentUserService.require(authentication));
    }

    @PatchMapping
    Dtos.ProfileResponse update(Authentication authentication, @Valid @RequestBody Dtos.ProfileUpdateRequest request) {
        return profileService.update(currentUserService.require(authentication), request);
    }

    @PostMapping("/skills")
    Dtos.SkillResponse addSkill(Authentication authentication, @Valid @RequestBody Dtos.ManualSkillRequest request) {
        return profileService.addSkill(currentUserService.require(authentication), request);
    }

    @PostMapping("/projects")
    Dtos.ProjectResponse addProject(Authentication authentication, @Valid @RequestBody Dtos.ProjectRequest request) {
        return profileService.addProject(currentUserService.require(authentication), request);
    }

    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Dtos.ResumeResponse uploadResume(Authentication authentication, @RequestPart("file") MultipartFile file) {
        return resumeService.upload(currentUserService.require(authentication), file);
    }

    @GetMapping("/resume")
    Dtos.ResumeResponse latestResume(Authentication authentication) {
        return resumeService.latest(currentUserService.require(authentication));
    }
}
