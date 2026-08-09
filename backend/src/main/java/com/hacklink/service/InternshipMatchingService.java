package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Internship;
import com.hacklink.entity.InternshipSkill;
import com.hacklink.entity.User;
import com.hacklink.repository.InternshipSkillRepository;
import com.hacklink.repository.ProjectRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InternshipMatchingService {
    private final InternshipSkillRepository internshipSkillRepository;
    private final UserSkillRepository userSkillRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Dtos.InternshipMatchResponse match(User user, Internship internship) {
        Set<String> userSkills = userSkillRepository.findAllByUserId(user.getId()).stream().map(skill -> skill.getSkill().getNormalizedName()).collect(java.util.stream.Collectors.toSet());
        List<InternshipSkill> requirements = internshipSkillRepository.findAllByInternshipId(internship.getId());
        Set<String> required = requirements.stream().filter(InternshipSkill::isRequired).map(skill -> skill.getSkill().getNormalizedName()).collect(java.util.stream.Collectors.toSet());
        Set<String> all = requirements.stream().map(skill -> skill.getSkill().getNormalizedName()).collect(java.util.stream.Collectors.toSet());
        Set<String> matched = new HashSet<>(userSkills);
        matched.retainAll(all);
        Set<String> gaps = new HashSet<>(required);
        gaps.removeAll(userSkills);
        int requiredScore = required.isEmpty() ? 100 : Math.round((required.stream().filter(userSkills::contains).count() * 100f) / required.size());
        int optionalScore = all.isEmpty() ? 100 : Math.round((matched.size() * 100f) / all.size());
        int evidenceScore = Math.min(100, projectRepository.findAllByUserId(user.getId()).size() * 25);
        int score = Math.round(requiredScore * 0.65f + optionalScore * 0.20f + evidenceScore * 0.15f);
        String reason = matched.isEmpty() ? "Build evidence for the required skills before applying." : "Your profile matches " + String.join(", ", matched) + ".";
        return new Dtos.InternshipMatchResponse(internship.getId(), score, matched.stream().sorted().toList(), gaps.stream().sorted().toList(), reason);
    }
}
