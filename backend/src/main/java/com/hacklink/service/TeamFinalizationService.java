package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.HackathonRegistration;
import com.hacklink.entity.InvitationStatus;
import com.hacklink.entity.NotificationType;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamMemberStatus;
import com.hacklink.entity.TeamStatus;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.HackathonRegistrationRepository;
import com.hacklink.repository.TeamInvitationRepository;
import com.hacklink.repository.TeamMemberRepository;
import com.hacklink.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamFinalizationService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;
    private final TeamInvitationRepository invitationRepository;
    private final HackathonRegistrationRepository registrationRepository;
    private final TeamSkillCoverageService coverageService;
    private final NotificationService notificationService;
    private final ApiMapper mapper;

    @Transactional
    public Dtos.TeamResponse finalizeTeam(User owner, UUID teamId) {
        Team team = teamRepository.findByIdForUpdate(teamId).orElseThrow(() -> new ApiException("TEAM_NOT_FOUND", "Team was not found.", HttpStatus.NOT_FOUND));
        if (!team.getOwner().getId().equals(owner.getId())) throw new ApiException("TEAM_OWNER_REQUIRED", "Only the team owner can finalize a team.", HttpStatus.FORBIDDEN);
        if (team.getStatus() == TeamStatus.SUBMITTED) return mapper.toTeam(team);
        long accepted = memberRepository.countByTeamIdAndStatus(teamId, TeamMemberStatus.ACCEPTED);
        if (accepted < team.getHackathon().getTeamSizeMin()) throw new ApiException("MINIMUM_TEAM_SIZE_NOT_MET", "Minimum " + team.getHackathon().getTeamSizeMin() + " members required.", HttpStatus.UNPROCESSABLE_ENTITY);
        if (accepted > team.getHackathon().getTeamSizeMax()) throw new ApiException("TEAM_SIZE_EXCEEDED", "Team cannot exceed " + team.getHackathon().getTeamSizeMax() + " members.", HttpStatus.UNPROCESSABLE_ENTITY);
        if (!invitationRepository.findAllByTeamIdAndStatus(teamId, InvitationStatus.PENDING).isEmpty()) throw new ApiException("PENDING_INVITATIONS", "Resolve pending team invitations before finalization.", HttpStatus.UNPROCESSABLE_ENTITY);
        Dtos.SkillCoverageResponse coverage = coverageService.coverage(team);
        if (!coverage.missingSkills().isEmpty()) throw new ApiException("REQUIRED_SKILLS_MISSING", "Team cannot be finalized. Missing: " + String.join(", ", coverage.missingSkills()) + ".", HttpStatus.UNPROCESSABLE_ENTITY);
        if (registrationRepository.findByHackathonIdAndUserId(team.getHackathon().getId(), owner.getId()).isEmpty()) throw new ApiException("HACKATHON_REGISTRATION_REQUIRED", "A valid hackathon registration is required.", HttpStatus.UNPROCESSABLE_ENTITY);
        team.setStatus(TeamStatus.SUBMITTED);
        Team saved = teamRepository.save(team);
        memberRepository.findAllByTeamId(teamId).stream().filter(member -> member.getStatus() == TeamMemberStatus.ACCEPTED && !member.getUser().getId().equals(owner.getId())).forEach(member -> notificationService.create(member.getUser(), NotificationType.TEAM_FINALIZED, "Team finalized", team.getName() + " is ready for the hackathon.", teamId));
        return mapper.toTeam(saved);
    }
}
