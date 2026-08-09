package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Hackathon;
import com.hacklink.entity.InvitationStatus;
import com.hacklink.entity.Profile;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamMember;
import com.hacklink.entity.TeamMemberStatus;
import com.hacklink.entity.User;
import com.hacklink.entity.UserSkill;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.HackathonRegistrationRepository;
import com.hacklink.repository.HackathonSkillRepository;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.ProjectRepository;
import com.hacklink.repository.TeamInvitationRepository;
import com.hacklink.repository.TeamMemberRepository;
import com.hacklink.repository.TeamRepository;
import com.hacklink.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamWorkflowRulesTest {
    @Mock TeamInvitationRepository invitationRepository;
    @Mock TeamRepository teamRepository;
    @Mock TeamMemberRepository memberRepository;
    @Mock UserRepository userRepository;
    @Mock HackathonRegistrationRepository registrationRepository;
    @Mock NotificationService notificationService;
    @Mock TeamSkillCoverageService coverageService;
    @Mock ApiMapper mapper;
    @Mock HackathonSkillRepository hackathonSkillRepository;
    @Mock ProfileRepository profileRepository;
    @Mock ProjectRepository projectRepository;
    @Mock com.hacklink.repository.UserSkillRepository userSkillRepository;

    @Test
    void rejectedInvitationCannotBeAccepted() {
        User receiver = user(UUID.randomUUID());
        com.hacklink.entity.TeamInvitation invitation = new com.hacklink.entity.TeamInvitation();
        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setReceiver(receiver);
        UUID invitationId = UUID.randomUUID();
        when(invitationRepository.findByIdForUpdate(invitationId)).thenReturn(Optional.of(invitation));
        InvitationService service = invitationService();

        assertThatThrownBy(() -> service.accept(receiver, invitationId))
                .isInstanceOf(ApiException.class)
                .hasMessage("This invitation is no longer pending.");
        verify(teamRepository, never()).findByIdForUpdate(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void expiredInvitationIsMarkedExpiredAndCannotBeAccepted() {
        User receiver = user(UUID.randomUUID());
        com.hacklink.entity.TeamInvitation invitation = new com.hacklink.entity.TeamInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setReceiver(receiver);
        invitation.setExpiresAt(Instant.now().minusSeconds(1));
        UUID invitationId = UUID.randomUUID();
        when(invitationRepository.findByIdForUpdate(invitationId)).thenReturn(Optional.of(invitation));
        InvitationService service = invitationService();

        assertThatThrownBy(() -> service.accept(receiver, invitationId))
                .isInstanceOf(ApiException.class)
                .hasMessage("This invitation has expired.");
        assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.EXPIRED);
        verify(invitationRepository).save(invitation);
    }

    @Test
    void selfInvitationIsRejected() {
        User owner = user(UUID.randomUUID());
        Team team = team(owner, 2, 5);
        when(teamRepository.findByIdForUpdate(team.getId())).thenReturn(Optional.of(team));
        InvitationService service = invitationService();

        assertThatThrownBy(() -> service.create(owner, team.getId(), new Dtos.InvitationRequest(owner.getId(), "hello")))
                .isInstanceOf(ApiException.class)
                .hasMessage("You cannot invite yourself.");
    }

    @Test
    void fullTeamCannotAcceptAnotherMember() {
        User owner = user(UUID.randomUUID());
        User receiver = user(UUID.randomUUID());
        Team team = team(owner, 2, 1);
        com.hacklink.entity.TeamInvitation invitation = new com.hacklink.entity.TeamInvitation(team, owner, receiver, null, Instant.now().plusSeconds(60));
        UUID invitationId = UUID.randomUUID();
        when(invitationRepository.findByIdForUpdate(invitationId)).thenReturn(Optional.of(invitation));
        when(teamRepository.findByIdForUpdate(team.getId())).thenReturn(Optional.of(team));
        when(memberRepository.countByTeamIdAndStatus(team.getId(), TeamMemberStatus.ACCEPTED)).thenReturn(1L);

        assertThatThrownBy(() -> invitationService().accept(receiver, invitationId))
                .isInstanceOf(ApiException.class)
                .hasMessage("This team has reached its maximum size.");
        verify(memberRepository, never()).save(org.mockito.ArgumentMatchers.any(TeamMember.class));
    }

    @Test
    void nonDiscoverableUsersAreExcludedFromRecommendations() {
        User owner = user(UUID.randomUUID());
        Team team = team(owner, 2, 5);
        Profile hidden = new Profile();
        hidden.setUser(user(UUID.randomUUID()));
        hidden.setDiscoverable(false);
        when(hackathonSkillRepository.findAllByHackathonId(team.getHackathon().getId())).thenReturn(List.of());
        when(memberRepository.findAllByTeamId(team.getId())).thenReturn(List.of(new TeamMember(team, owner, "Owner", TeamMemberStatus.ACCEPTED)));
        when(profileRepository.findAllByDiscoverableTrue()).thenReturn(List.of());

        List<Dtos.TeamMatchResponse> matches = new TeamMatchingService(hackathonSkillRepository, profileRepository, memberRepository, userSkillRepository, projectRepository).recommend(team);

        assertThat(matches).isEmpty();
    }

    private InvitationService invitationService() {
        return new InvitationService(invitationRepository, teamRepository, memberRepository, userRepository, registrationRepository, notificationService, coverageService, mapper);
    }

    private User user(UUID id) {
        User user = new User();
        user.setId(id);
        user.setEmail(id + "@example.com");
        return user;
    }

    private Team team(User owner, int minimum, int maximum) {
        Hackathon hackathon = new Hackathon();
        hackathon.setId(UUID.randomUUID());
        hackathon.setTeamSizeMin(minimum);
        hackathon.setTeamSizeMax(maximum);
        Team team = new Team("Team", owner, hackathon);
        team.setId(UUID.randomUUID());
        return team;
    }
}
