package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Profile;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamMemberStatus;
import com.hacklink.entity.UserSkill;
import com.hacklink.repository.HackathonSkillRepository;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.ProjectRepository;
import com.hacklink.repository.TeamMemberRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamMatchingService {
    private final HackathonSkillRepository hackathonSkillRepository;
    private final ProfileRepository profileRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserSkillRepository userSkillRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Dtos.TeamMatchResponse> recommend(Team team) {
        Set<String> required = new HashSet<>(hackathonSkillRepository.findAllByHackathonId(team.getHackathon().getId()).stream().map(skill -> skill.getSkill().getNormalizedName()).toList());
        Set<String> covered = new HashSet<>();
        teamMemberRepository.findAllByTeamId(team.getId()).stream().filter(member -> member.getStatus() == TeamMemberStatus.ACCEPTED).forEach(member -> userSkillRepository.findAllByUserId(member.getUser().getId()).forEach(skill -> covered.add(skill.getSkill().getNormalizedName())));
        Set<String> missing = new HashSet<>(required);
        missing.removeAll(covered);
        Set<java.util.UUID> currentMembers = teamMemberRepository.findAllByTeamId(team.getId()).stream().map(member -> member.getUser().getId()).collect(java.util.stream.Collectors.toSet());

        return profileRepository.findAllByDiscoverableTrue().stream()
                .filter(profile -> !currentMembers.contains(profile.getUser().getId()))
                .filter(profile -> teamMemberRepository.findAllByUserIdAndStatusIn(profile.getUser().getId(), List.of(TeamMemberStatus.ACCEPTED, TeamMemberStatus.INVITED, TeamMemberStatus.PENDING)).stream().noneMatch(member -> member.getTeam().getHackathon().getId().equals(team.getHackathon().getId())))
                .map(profile -> score(profile, missing, required))
                .sorted(java.util.Comparator.comparingInt(Dtos.TeamMatchResponse::compatibilityScore).reversed())
                .limit(25)
                .toList();
    }

    private Dtos.TeamMatchResponse score(Profile profile, Set<String> missing, Set<String> required) {
        List<UserSkill> skills = userSkillRepository.findAllByUserId(profile.getUser().getId());
        Set<String> candidateSkills = skills.stream().map(skill -> skill.getSkill().getNormalizedName()).collect(java.util.stream.Collectors.toSet());
        Set<String> coveredMissing = new HashSet<>(candidateSkills);
        coveredMissing.retainAll(missing);
        Set<String> relevant = new HashSet<>(candidateSkills);
        relevant.retainAll(required);
        int complementarity = missing.isEmpty() ? 0 : Math.round((coveredMissing.size() * 100f) / missing.size());
        int relevance = required.isEmpty() ? 0 : Math.round((relevant.size() * 100f) / required.size());
        int evidence = Math.min(100, projectRepository.findAllByUserId(profile.getUser().getId()).size() * 25);
        int experience = switch (profile.getExperienceLevel() == null ? "" : profile.getExperienceLevel().name()) {
            case "EXPERT" -> 100;
            case "ADVANCED" -> 85;
            case "INTERMEDIATE" -> 70;
            default -> 50;
        };
        int availability = profile.getAvailability() == null || profile.getAvailability().isBlank() ? 45 : 85;
        int score = Math.round(complementarity * 0.40f + relevance * 0.20f + evidence * 0.15f + experience * 0.10f + availability * 0.15f);
        List<String> reasons = new java.util.ArrayList<>();
        if (!coveredMissing.isEmpty()) reasons.add("Fills " + coveredMissing.stream().map(this::displaySkill).sorted().collect(java.util.stream.Collectors.joining(", ")) + " gap");
        if (!relevant.isEmpty()) reasons.add("Relevant to the selected hackathon");
        if (evidence > 0) reasons.add("Has project evidence");
        if (availability >= 80) reasons.add("Availability is listed");
        return new Dtos.TeamMatchResponse(profile.getUser().getId(), profile.getFullName(), profile.getHeadline(), score,
                coveredMissing.stream().map(this::displaySkill).sorted().toList(), reasons, profile.getAvailability());
    }

    private String displaySkill(String normalized) {
        return normalized.isBlank() ? normalized : Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }
}
