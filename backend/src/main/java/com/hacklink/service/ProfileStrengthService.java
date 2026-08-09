package com.hacklink.service;

import com.hacklink.entity.Profile;
import com.hacklink.repository.GithubAccountRepository;
import com.hacklink.repository.ProjectRepository;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.ResumeRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileStrengthService {
    private final ProfileRepository profileRepository;
    private final UserSkillRepository userSkillRepository;
    private final ProjectRepository projectRepository;
    private final ResumeRepository resumeRepository;
    private final GithubAccountRepository githubAccountRepository;

    @Transactional
    public int recalculate(UUID userId) {
        Profile profile = profileRepository.findByUserId(userId).orElse(null);
        if (profile == null) {
            return 0;
        }
        int score = 0;
        if (notBlank(profile.getFullName())) score += 15;
        if (notBlank(profile.getHeadline())) score += 10;
        if (notBlank(profile.getBio())) score += 10;
        if (notBlank(profile.getLocation())) score += 5;
        if (notBlank(profile.getAvailability())) score += 5;
        score += Math.min(25, userSkillRepository.findAllByUserId(userId).size() * 5);
        score += Math.min(15, projectRepository.findAllByUserId(userId).size() * 5);
        if (resumeRepository.findTopByUserIdOrderByUploadedAtDesc(userId).isPresent()) score += 10;
        if (githubAccountRepository.findByUserId(userId).isPresent()) score += 5;
        profile.setProfileStrength(Math.min(100, score));
        profileRepository.save(profile);
        return profile.getProfileStrength();
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
