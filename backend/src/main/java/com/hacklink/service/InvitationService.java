package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.InvitationStatus;
import com.hacklink.entity.NotificationType;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamInvitation;
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
import com.hacklink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final TeamInvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final HackathonRegistrationRepository registrationRepository;
    private final NotificationService notificationService;
    private final TeamSkillCoverageService coverageService;
    private final ApiMapper mapper;

    @Transactional(readOnly = true)
    public List<Dtos.InvitationResponse> pending(User receiver) {
        expireOldInvitations(receiver);
        return invitationRepository.findAllByReceiverIdAndStatusOrderByCreatedAtDesc(receiver.getId(), InvitationStatus.PENDING).stream().map(mapper::toInvitation).toList();
    }

    @Transactional
    public Dtos.InvitationResponse create(User sender, UUID teamId, Dtos.InvitationRequest request) {
        Team team = teamRepository.findByIdForUpdate(teamId).orElseThrow(() -> new ApiException("TEAM_NOT_FOUND", "Team was not found.", HttpStatus.NOT_FOUND));
        if (!team.getOwner().getId().equals(sender.getId())) {
            throw new ApiException("TEAM_OWNER_REQUIRED", "Only the team owner can invite members.", HttpStatus.FORBIDDEN);
        }
        if (sender.getId().equals(request.receiverId())) {
            throw new ApiException("SELF_INVITATION", "You cannot invite yourself.", HttpStatus.BAD_REQUEST);
        }
        if (team.getStatus() == TeamStatus.FULL || team.getStatus() == TeamStatus.SUBMITTED || team.getStatus() == TeamStatus.CANCELLED) {
            throw new ApiException("TEAM_NOT_RECRUITING", "This team is not accepting new invitations.", HttpStatus.CONFLICT);
        }
        if (memberRepository.existsByTeamIdAndUserId(teamId, request.receiverId())) {
            throw new ApiException("ALREADY_TEAM_MEMBER", "This user is already associated with the team.", HttpStatus.CONFLICT);
        }
        if (invitationRepository.existsByTeamIdAndReceiverIdAndStatus(teamId, request.receiverId(), InvitationStatus.PENDING)) {
            throw new ApiException("DUPLICATE_INVITATION", "A pending invitation already exists for this user.", HttpStatus.CONFLICT);
        }
        User receiver = userRepository.findById(request.receiverId()).orElseThrow(() -> new ApiException("USER_NOT_FOUND", "The invited user was not found.", HttpStatus.NOT_FOUND));
        TeamInvitation invitation = invitationRepository.save(new TeamInvitation(team, sender, receiver, request.message(), Instant.now().plus(7, ChronoUnit.DAYS)));
        notificationService.create(receiver, NotificationType.TEAM_INVITATION, "Team invitation", "You have been invited to " + team.getName() + ".", invitation.getId());
        return mapper.toInvitation(invitation);
    }

    @Transactional
    public Dtos.TeamResponse accept(User receiver, UUID invitationId) {
        TeamInvitation invitation = invitationRepository.findByIdForUpdate(invitationId).orElseThrow(() -> new ApiException("INVITATION_NOT_FOUND", "Invitation was not found.", HttpStatus.NOT_FOUND));
        if (!invitation.getReceiver().getId().equals(receiver.getId())) throw new ApiException("FORBIDDEN", "This invitation is not addressed to you.", HttpStatus.FORBIDDEN);
        if (invitation.getStatus() != InvitationStatus.PENDING) throw new ApiException("INVITATION_NOT_PENDING", "This invitation is no longer pending.", HttpStatus.CONFLICT);
        if (invitation.getExpiresAt().isBefore(Instant.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitation.setRespondedAt(Instant.now());
            invitationRepository.save(invitation);
            throw new ApiException("INVITATION_EXPIRED", "This invitation has expired.", HttpStatus.CONFLICT);
        }
        Team team = teamRepository.findByIdForUpdate(invitation.getTeam().getId()).orElseThrow(() -> new ApiException("TEAM_NOT_FOUND", "Team was not found.", HttpStatus.NOT_FOUND));
        long accepted = memberRepository.countByTeamIdAndStatus(team.getId(), TeamMemberStatus.ACCEPTED);
        if (accepted >= team.getHackathon().getTeamSizeMax()) throw new ApiException("TEAM_FULL", "This team has reached its maximum size.", HttpStatus.CONFLICT);
        if (memberRepository.existsByTeamIdAndUserId(team.getId(), receiver.getId())) throw new ApiException("ALREADY_TEAM_MEMBER", "You are already a member of this team.", HttpStatus.CONFLICT);
        boolean incompatible = memberRepository.findAllByUserIdAndStatusIn(receiver.getId(), List.of(TeamMemberStatus.ACCEPTED)).stream().anyMatch(member -> member.getTeam().getHackathon().getId().equals(team.getHackathon().getId()));
        if (incompatible) throw new ApiException("INCOMPATIBLE_TEAM_MEMBERSHIP", "You are already committed to another team for this hackathon.", HttpStatus.CONFLICT);
        memberRepository.save(new TeamMember(team, receiver, null, TeamMemberStatus.ACCEPTED));
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRespondedAt(Instant.now());
        invitationRepository.save(invitation);
        if (accepted + 1 >= team.getHackathon().getTeamSizeMax()) team.setStatus(TeamStatus.FULL); else team.setStatus(TeamStatus.RECRUITING);
        teamRepository.save(team);
        notificationService.create(invitation.getSender(), NotificationType.INVITATION_ACCEPTED, "Invitation accepted", receiver.getEmail() + " accepted your team invitation.", team.getId());
        return mapper.toTeam(team);
    }

    @Transactional
    public void cancel(User sender, UUID invitationId) {
        TeamInvitation invitation = invitationRepository.findByIdForUpdate(invitationId).orElseThrow(() -> new ApiException("INVITATION_NOT_FOUND", "Invitation was not found.", HttpStatus.NOT_FOUND));
        if (!invitation.getSender().getId().equals(sender.getId())) throw new ApiException("FORBIDDEN", "Only the sender can cancel this invitation.", HttpStatus.FORBIDDEN);
        if (invitation.getStatus() != InvitationStatus.PENDING) throw new ApiException("INVITATION_NOT_PENDING", "This invitation is no longer pending.", HttpStatus.CONFLICT);
        invitation.setStatus(InvitationStatus.CANCELLED);
        invitation.setRespondedAt(Instant.now());
        invitationRepository.save(invitation);
    }

    @Transactional
    public Dtos.InvitationResponse reject(User receiver, UUID invitationId) {
        TeamInvitation invitation = invitationRepository.findByIdForUpdate(invitationId).orElseThrow(() -> new ApiException("INVITATION_NOT_FOUND", "Invitation was not found.", HttpStatus.NOT_FOUND));
        if (!invitation.getReceiver().getId().equals(receiver.getId())) throw new ApiException("FORBIDDEN", "This invitation is not addressed to you.", HttpStatus.FORBIDDEN);
        if (invitation.getStatus() != InvitationStatus.PENDING) throw new ApiException("INVITATION_NOT_PENDING", "This invitation is no longer pending.", HttpStatus.CONFLICT);
        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setRespondedAt(Instant.now());
        notificationService.create(invitation.getSender(), NotificationType.INVITATION_REJECTED, "Invitation declined", receiver.getEmail() + " declined your team invitation.", invitation.getTeam().getId());
        return mapper.toInvitation(invitationRepository.save(invitation));
    }

    private void expireOldInvitations(User receiver) {
        invitationRepository.findAllByReceiverIdAndStatusOrderByCreatedAtDesc(receiver.getId(), InvitationStatus.PENDING).stream().filter(invitation -> invitation.getExpiresAt().isBefore(Instant.now())).forEach(invitation -> {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitation.setRespondedAt(Instant.now());
            invitationRepository.save(invitation);
        });
    }
}
