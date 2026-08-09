package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Hackathon;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamMember;
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final HackathonRegistrationRepository registrationRepository;
    private final TeamInvitationRepository invitationRepository;
    private final HackathonService hackathonService;
    private final TeamSkillCoverageService coverageService;
    private final TeamMatchingService matchingService;
    private final ApiMapper mapper;

    @Transactional
    public Dtos.TeamResponse create(User owner, Dtos.CreateTeamRequest request) {
        Hackathon hackathon = hackathonService.require(request.hackathonId());
        if (registrationRepository.findByHackathonIdAndUserId(hackathon.getId(), owner.getId()).isEmpty()) {
            throw new ApiException("HACKATHON_REGISTRATION_REQUIRED", "Register for the hackathon before creating a team.", HttpStatus.CONFLICT);
        }
        Team team = teamRepository.save(new Team(request.name().trim(), owner, hackathon));
        teamMemberRepository.save(new TeamMember(team, owner, "Team owner", TeamMemberStatus.ACCEPTED));
        team.setStatus(TeamStatus.RECRUITING);
        return mapper.toTeam(teamRepository.save(team));
    }

    @Transactional(readOnly = true)
    public List<Dtos.TeamResponse> ownedBy(User owner) {
        return teamRepository.findAllByOwnerId(owner.getId()).stream().map(mapper::toTeam).toList();
    }

    @Transactional(readOnly = true)
    public Dtos.TeamResponse get(User user, UUID teamId) {
        Team team = require(teamId);
        requireAccess(team, user, false);
        return mapper.toTeam(team);
    }

    @Transactional(readOnly = true)
    public Dtos.SkillCoverageResponse coverage(User user, UUID teamId) {
        Team team = require(teamId);
        requireAccess(team, user, false);
        return coverageService.coverage(team);
    }

    @Transactional(readOnly = true)
    public List<Dtos.TeamMatchResponse> recommendations(User user, UUID teamId) {
        Team team = require(teamId);
        requireAccess(team, user, false);
        return matchingService.recommend(team);
    }

    private void requireOwner(Team team, User user) {
        if (!team.getOwner().getId().equals(user.getId())) {
            throw new ApiException("TEAM_OWNER_REQUIRED", "Only the team owner can perform this action.", HttpStatus.FORBIDDEN);
        }
    }

    private void requireAccess(Team team, User user, boolean ownerOnly) {
        if (ownerOnly) {
            requireOwner(team, user);
            return;
        }
        boolean member = team.getOwner().getId().equals(user.getId()) || teamMemberRepository.findByTeamIdAndUserId(team.getId(), user.getId()).filter(item -> item.getStatus() == TeamMemberStatus.ACCEPTED).isPresent();
        if (!member) {
            throw new ApiException("TEAM_ACCESS_REQUIRED", "You are not a member of this team.", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public void leave(User user, UUID teamId) {
        Team team = teamRepository.findByIdForUpdate(teamId).orElseThrow(() -> new ApiException("TEAM_NOT_FOUND", "Team was not found.", HttpStatus.NOT_FOUND));
        if (team.getOwner().getId().equals(user.getId())) throw new ApiException("OWNER_CANNOT_LEAVE", "The owner must transfer or cancel the team before leaving.", HttpStatus.CONFLICT);
        TeamMember member = teamMemberRepository.findByTeamIdAndUserId(teamId, user.getId()).orElseThrow(() -> new ApiException("TEAM_MEMBER_NOT_FOUND", "You are not a member of this team.", HttpStatus.NOT_FOUND));
        member.setStatus(TeamMemberStatus.LEFT);
        teamMemberRepository.save(member);
        if (team.getStatus() == TeamStatus.FULL) {
            team.setStatus(TeamStatus.RECRUITING);
            teamRepository.save(team);
        }
    }

    @Transactional
    public void remove(User owner, UUID teamId, UUID userId) {
        Team team = teamRepository.findByIdForUpdate(teamId).orElseThrow(() -> new ApiException("TEAM_NOT_FOUND", "Team was not found.", HttpStatus.NOT_FOUND));
        requireOwner(team, owner);
        if (team.getOwner().getId().equals(userId)) throw new ApiException("OWNER_CANNOT_REMOVE", "The owner cannot be removed.", HttpStatus.BAD_REQUEST);
        TeamMember member = teamMemberRepository.findByTeamIdAndUserId(teamId, userId).orElseThrow(() -> new ApiException("TEAM_MEMBER_NOT_FOUND", "Team member was not found.", HttpStatus.NOT_FOUND));
        member.setStatus(TeamMemberStatus.REMOVED);
        teamMemberRepository.save(member);
        if (team.getStatus() == TeamStatus.FULL) {
            team.setStatus(TeamStatus.RECRUITING);
            teamRepository.save(team);
        }
    }

    public Team require(UUID teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new ApiException("TEAM_NOT_FOUND", "Team was not found.", HttpStatus.NOT_FOUND));
    }

}
