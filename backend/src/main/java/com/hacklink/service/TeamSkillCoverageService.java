package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamMemberStatus;
import com.hacklink.repository.HackathonSkillRepository;
import com.hacklink.repository.TeamMemberRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamSkillCoverageService {
    private final HackathonSkillRepository hackathonSkillRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional(readOnly = true)
    public Dtos.SkillCoverageResponse coverage(Team team) {
        List<String> required = hackathonSkillRepository.findAllByHackathonIdAndRequiredTrue(team.getHackathon().getId()).stream().map(skill -> skill.getSkill().getName()).toList();
        Set<String> covered = new LinkedHashSet<>();
        teamMemberRepository.findAllByTeamId(team.getId()).stream().filter(member -> member.getStatus() == TeamMemberStatus.ACCEPTED).forEach(member -> userSkillRepository.findAllByUserId(member.getUser().getId()).forEach(skill -> covered.add(skill.getSkill().getName())));
        List<String> coveredRequired = required.stream().filter(covered::contains).toList();
        List<String> missing = required.stream().filter(skill -> !covered.contains(skill)).toList();
        int percentage = required.isEmpty() ? 100 : Math.round((coveredRequired.size() * 100f) / required.size());
        return new Dtos.SkillCoverageResponse(required, coveredRequired, missing, percentage);
    }
}
