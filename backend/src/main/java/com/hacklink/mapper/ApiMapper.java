package com.hacklink.mapper;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Hackathon;
import com.hacklink.entity.HackathonSkill;
import com.hacklink.entity.HackathonTrack;
import com.hacklink.entity.Internship;
import com.hacklink.entity.InternshipApplication;
import com.hacklink.entity.InternshipSkill;
import com.hacklink.entity.Notification;
import com.hacklink.entity.Profile;
import com.hacklink.entity.Project;
import com.hacklink.entity.SkillEvidence;
import com.hacklink.entity.Team;
import com.hacklink.entity.TeamInvitation;
import com.hacklink.entity.TeamMember;
import com.hacklink.entity.UserSkill;
import com.hacklink.repository.HackathonSkillRepository;
import com.hacklink.repository.HackathonTrackRepository;
import com.hacklink.repository.InternshipApplicationRepository;
import com.hacklink.repository.InternshipSkillRepository;
import com.hacklink.repository.ProjectRepository;
import com.hacklink.repository.SavedInternshipRepository;
import com.hacklink.repository.SkillEvidenceRepository;
import com.hacklink.repository.TeamMemberRepository;
import com.hacklink.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApiMapper {
    private final UserSkillRepository userSkillRepository;
    private final SkillEvidenceRepository skillEvidenceRepository;
    private final ProjectRepository projectRepository;
    private final HackathonSkillRepository hackathonSkillRepository;
    private final HackathonTrackRepository hackathonTrackRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final InternshipSkillRepository internshipSkillRepository;
    private final InternshipApplicationRepository internshipApplicationRepository;
    private final SavedInternshipRepository savedInternshipRepository;

    public Dtos.ProfileResponse toProfile(Profile profile) {
        UUID userId = profile.getUser().getId();
        List<Dtos.SkillResponse> skills = userSkillRepository.findAllByUserId(userId).stream().map(this::toSkill).toList();
        List<Dtos.ProjectResponse> projects = projectRepository.findAllByUserId(userId).stream().map(this::toProject).toList();
        return new Dtos.ProfileResponse(userId, profile.getId(), profile.getPublicProfileId(), profile.getUser().getEmail(),
                profile.getFullName(), profile.getHeadline(), profile.getRole(), profile.getExperienceLevel(), profile.getBio(),
                profile.getLocation(), profile.getAvailability(), profile.getProfileImageUrl(), profile.getProfileStrength(),
                profile.isDiscoverable(), skills, projects);
    }

    public Dtos.PublicProfileResponse toPublicProfile(Profile profile) {
        return new Dtos.PublicProfileResponse(profile.getPublicProfileId(), profile.getFullName(), profile.getHeadline(),
                profile.getRole(), profile.getLocation(), userSkillRepository.findAllByUserId(profile.getUser().getId()).stream().map(this::toSkill).toList());
    }

    public Dtos.SkillResponse toSkill(UserSkill userSkill) {
        List<Dtos.SkillEvidenceResponse> evidence = skillEvidenceRepository.findAllByUserSkillId(userSkill.getId()).stream()
                .map(this::toEvidence).toList();
        return new Dtos.SkillResponse(userSkill.getSkill().getId(), userSkill.getSkill().getName(), userSkill.getConfidence(),
                evidence.stream().map(Dtos.SkillEvidenceResponse::source).distinct().toList(), evidence);
    }

    public Dtos.SkillEvidenceResponse toEvidence(SkillEvidence evidence) {
        return new Dtos.SkillEvidenceResponse(evidence.getId(), evidence.getSource(), evidence.getTitle(), evidence.getDescription(), evidence.getSourceReference());
    }

    public Dtos.ProjectResponse toProject(Project project) {
        return new Dtos.ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getUrl(), project.getSource(), project.getCreatedAt());
    }

    public Dtos.HackathonResponse toHackathon(Hackathon hackathon, UUID userId) {
        return toHackathon(hackathon, userId, false);
    }

    public Dtos.HackathonResponse toHackathon(Hackathon hackathon, UUID userId, boolean registered) {
        List<Dtos.HackathonSkillResponse> skills = hackathonSkillRepository.findAllByHackathonId(hackathon.getId()).stream()
                .map(this::toHackathonSkill).toList();
        List<Dtos.HackathonTrackResponse> tracks = hackathonTrackRepository.findAllByHackathonId(hackathon.getId()).stream()
                .map(this::toHackathonTrack).toList();
        return new Dtos.HackathonResponse(hackathon.getId(), hackathon.getName(), hackathon.getDescription(), hackathon.getOrganizer(),
                hackathon.getStartDate(), hackathon.getEndDate(), hackathon.getRegistrationDeadline(), hackathon.getMode(), hackathon.getLocation(),
                hackathon.getTeamSizeMin(), hackathon.getTeamSizeMax(), hackathon.getStatus(), hackathon.getRegistrationUrl(), registered, skills, tracks);
    }

    public Dtos.HackathonSkillResponse toHackathonSkill(HackathonSkill skill) {
        return new Dtos.HackathonSkillResponse(skill.getSkill().getName(), skill.getImportance(), skill.isRequired());
    }

    public Dtos.HackathonTrackResponse toHackathonTrack(HackathonTrack track) {
        return new Dtos.HackathonTrackResponse(track.getId(), track.getName(), track.getDescription());
    }

    public Dtos.TeamResponse toTeam(Team team) {
        List<Dtos.TeamMemberResponse> members = teamMemberRepository.findAllByTeamId(team.getId()).stream().map(this::toTeamMember).toList();
        return new Dtos.TeamResponse(team.getId(), team.getName(), team.getOwner().getId(), team.getHackathon().getId(), team.getStatus(), members);
    }

    public Dtos.TeamMemberResponse toTeamMember(TeamMember member) {
        String fullName = member.getUser().getProfile() == null ? member.getUser().getEmail() : member.getUser().getProfile().getFullName();
        return new Dtos.TeamMemberResponse(member.getUser().getId(), fullName, member.getRole(), member.getStatus(), member.getJoinedAt());
    }

    public Dtos.InvitationResponse toInvitation(TeamInvitation invitation) {
        return new Dtos.InvitationResponse(invitation.getId(), invitation.getTeam().getId(), invitation.getTeam().getName(),
                invitation.getSender().getId(), invitation.getReceiver().getId(), invitation.getMessage(), invitation.getStatus(),
                invitation.getCreatedAt(), invitation.getRespondedAt(), invitation.getExpiresAt());
    }

    public Dtos.InternshipResponse toInternship(Internship internship, UUID userId) {
        boolean saved = userId != null && savedInternshipRepository.findByInternshipIdAndUserId(internship.getId(), userId).isPresent();
        InternshipApplication application = userId == null ? null : internshipApplicationRepository.findByInternshipIdAndUserId(internship.getId(), userId).orElse(null);
        List<Dtos.InternshipSkillResponse> skills = internshipSkillRepository.findAllByInternshipId(internship.getId()).stream().map(this::toInternshipSkill).toList();
        return new Dtos.InternshipResponse(internship.getId(), internship.getCompany(), internship.getTitle(), internship.getDescription(),
                internship.getLocation(), internship.getWorkMode(), internship.getApplicationDeadline(), internship.getApplicationUrl(), internship.getStatus(),
                saved, application == null ? null : application.getStatus(), skills);
    }

    public Dtos.InternshipSkillResponse toInternshipSkill(InternshipSkill skill) {
        return new Dtos.InternshipSkillResponse(skill.getSkill().getName(), skill.isRequired());
    }

    public Dtos.ConnectionResponse toConnection(com.hacklink.entity.Connection connection) {
        return new Dtos.ConnectionResponse(connection.getId(), connection.getRequester().getId(), connection.getReceiver().getId(), connection.getStatus(), connection.getCreatedAt(), connection.getUpdatedAt());
    }

    public Dtos.NotificationResponse toNotification(Notification notification) {
        return new Dtos.NotificationResponse(notification.getId(), notification.getType(), notification.getTitle(), notification.getBody(), notification.getReferenceId(), notification.getReadAt() != null, notification.getCreatedAt());
    }
}
