package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Hackathon;
import com.hacklink.entity.HackathonSkill;
import com.hacklink.entity.User;
import com.hacklink.repository.HackathonSkillRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HackathonMatchingService {
    private final HackathonSkillRepository hackathonSkillRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional(readOnly = true)
    public Dtos.HackathonMatchResponse match(User user, Hackathon hackathon) {
        Map<String, Integer> userSkills = new HashMap<>();
        userSkillRepository.findAllByUserId(user.getId()).forEach(skill -> userSkills.put(skill.getSkill().getNormalizedName(), skill.getConfidence()));
        List<HackathonSkill> requirements = hackathonSkillRepository.findAllByHackathonId(hackathon.getId());
        int totalImportance = requirements.stream().mapToInt(HackathonSkill::getImportance).sum();
        int matchedImportance = requirements.stream().filter(requirement -> userSkills.containsKey(requirement.getSkill().getNormalizedName())).mapToInt(HackathonSkill::getImportance).sum();
        int score = totalImportance == 0 ? 0 : Math.round((matchedImportance * 100f) / totalImportance);
        List<String> matched = requirements.stream().filter(requirement -> userSkills.containsKey(requirement.getSkill().getNormalizedName())).map(requirement -> requirement.getSkill().getName()).toList();
        List<String> missing = requirements.stream().filter(requirement -> !userSkills.containsKey(requirement.getSkill().getNormalizedName())).map(requirement -> requirement.getSkill().getName()).toList();
        String reason = matched.isEmpty() ? "Add evidence-backed skills that match this hackathon's requirements." : "Your evidence-backed profile covers " + String.join(", ", matched) + ".";
        return new Dtos.HackathonMatchResponse(score, matched, missing, List.of(reason));
    }
}
