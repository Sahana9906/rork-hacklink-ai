package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Resume;
import com.hacklink.entity.SkillSource;
import com.hacklink.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillExtractionService {
    private final SkillService skillService;

    @Transactional
    public List<Dtos.SkillResponse> extractFromResume(User user, Resume resume, ResumeParserService.ParsedResume parsed) {
        return parsed.skills().stream()
                .map(skill -> skillService.addSkill(user, skill, 70, SkillSource.RESUME,
                        "Resume evidence", "Skill extracted from the uploaded resume.", resume.getOriginalFileName()))
                .toList();
    }
}
