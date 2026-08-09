package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Profile;
import com.hacklink.entity.Project;
import com.hacklink.entity.SkillSource;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProjectRepository projectRepository;
    private final SkillService skillService;
    private final ProfileStrengthService profileStrengthService;
    private final ApiMapper mapper;

    @Transactional(readOnly = true)
    public Dtos.ProfileResponse get(User user) {
        return mapper.toProfile(requireProfile(user));
    }

    @Transactional
    public Dtos.ProfileResponse update(User user, Dtos.ProfileUpdateRequest request) {
        Profile profile = requireProfile(user);
        if (request.fullName() != null) profile.setFullName(request.fullName().trim());
        if (request.headline() != null) profile.setHeadline(request.headline().trim());
        if (request.role() != null) profile.setRole(request.role().trim());
        if (request.experienceLevel() != null) profile.setExperienceLevel(request.experienceLevel());
        if (request.bio() != null) profile.setBio(request.bio().trim());
        if (request.location() != null) profile.setLocation(request.location().trim());
        if (request.availability() != null) profile.setAvailability(request.availability().trim());
        if (request.profileImageUrl() != null) profile.setProfileImageUrl(request.profileImageUrl().trim());
        if (request.discoverable() != null) profile.setDiscoverable(request.discoverable());
        profileRepository.save(profile);
        profileStrengthService.recalculate(user.getId());
        return mapper.toProfile(requireProfile(user));
    }

    @Transactional
    public Dtos.SkillResponse addSkill(User user, Dtos.ManualSkillRequest request) {
        Dtos.SkillResponse response = skillService.addManualSkill(user, request);
        profileStrengthService.recalculate(user.getId());
        return response;
    }

    @Transactional
    public Dtos.ProjectResponse addProject(User user, Dtos.ProjectRequest request) {
        Project project = projectRepository.save(new Project(user, request.name().trim(), request.description(), request.url(), SkillSource.MANUAL));
        profileStrengthService.recalculate(user.getId());
        return mapper.toProject(project);
    }

    public Profile requireProfile(User user) {
        return profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException("PROFILE_NOT_FOUND", "Profile was not found.", HttpStatus.NOT_FOUND));
    }
}
