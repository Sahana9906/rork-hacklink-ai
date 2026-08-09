package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Skill;
import com.hacklink.entity.SkillEvidence;
import com.hacklink.entity.SkillSource;
import com.hacklink.entity.User;
import com.hacklink.entity.UserSkill;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.SkillEvidenceRepository;
import com.hacklink.repository.SkillRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillEvidenceRepository evidenceRepository;
    private final ApiMapper mapper;

    @Transactional
    public Dtos.SkillResponse addManualSkill(User user, Dtos.ManualSkillRequest request) {
        return addSkill(user, request.name(), request.confidence(), SkillSource.MANUAL,
                request.evidenceTitle() == null || request.evidenceTitle().isBlank() ? "Manual skill declaration" : request.evidenceTitle(),
                request.evidenceDescription(), null);
    }

    @Transactional
    public Dtos.SkillResponse addSkill(User user, String name, int confidence, SkillSource source, String evidenceTitle, String description, String sourceReference) {
        String normalized = name.trim().toLowerCase(Locale.ROOT);
        Skill skill = skillRepository.findByNormalizedName(normalized).orElseGet(() -> skillRepository.save(new Skill(name)));
        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId()).orElseGet(() -> new UserSkill(user, skill, confidence));
        userSkill.setConfidence(Math.max(userSkill.getConfidence(), Math.min(100, confidence)));
        userSkill = userSkillRepository.save(userSkill);
        if (evidenceTitle != null && !evidenceTitle.isBlank()) {
            evidenceRepository.save(new SkillEvidence(userSkill, source, evidenceTitle, description, sourceReference));
        }
        return mapper.toSkill(userSkill);
    }

    @Transactional(readOnly = true)
    public List<Dtos.SkillResponse> list(User user) {
        return userSkillRepository.findAllByUserId(user.getId()).stream().map(mapper::toSkill).toList();
    }

    public Skill findOrCreate(String name) {
        String normalized = name.trim().toLowerCase(Locale.ROOT);
        return skillRepository.findByNormalizedName(normalized).orElseGet(() -> skillRepository.save(new Skill(name)));
    }
}
