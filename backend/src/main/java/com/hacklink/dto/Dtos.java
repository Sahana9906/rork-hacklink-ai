package com.hacklink.dto;

import com.hacklink.entity.ApplicationStatus;
import com.hacklink.entity.ConnectionStatus;
import com.hacklink.entity.ExperienceLevel;
import com.hacklink.entity.HackathonMode;
import com.hacklink.entity.HackathonStatus;
import com.hacklink.entity.InvitationStatus;
import com.hacklink.entity.InternshipStatus;
import com.hacklink.entity.NotificationType;
import com.hacklink.entity.ResumeStatus;
import com.hacklink.entity.SkillSource;
import com.hacklink.entity.TeamMemberStatus;
import com.hacklink.entity.TeamStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Dtos {
    private Dtos() {
    }

    public record RegisterRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @NotBlank @Size(max = 160) String fullName
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record AuthResponse(String accessToken, String tokenType, Instant expiresAt, ProfileResponse profile) {
    }

    public record ProfileUpdateRequest(
            @Size(max = 160) String fullName,
            @Size(max = 240) String headline,
            @Size(max = 120) String role,
            ExperienceLevel experienceLevel,
            @Size(max = 4000) String bio,
            @Size(max = 160) String location,
            @Size(max = 120) String availability,
            @Size(max = 500) String profileImageUrl,
            Boolean discoverable
    ) {
    }

    public record ManualSkillRequest(
            @NotBlank @Size(max = 120) String name,
            @Min(0) @Max(100) int confidence,
            @Size(max = 240) String evidenceTitle,
            @Size(max = 2000) String evidenceDescription
    ) {
    }

    public record ProjectRequest(
            @NotBlank @Size(max = 200) String name,
            @Size(max = 4000) String description,
            @Size(max = 500) String url
    ) {
    }

    public record SkillEvidenceResponse(UUID id, SkillSource source, String title, String description, String sourceReference) {
    }

    public record SkillResponse(UUID id, String name, int confidence, List<SkillSource> sources, List<SkillEvidenceResponse> evidence) {
    }

    public record ProjectResponse(UUID id, String name, String description, String url, SkillSource source, Instant createdAt) {
    }

    public record ProfileResponse(
            UUID userId,
            UUID profileId,
            UUID publicProfileId,
            String email,
            String fullName,
            String headline,
            String role,
            ExperienceLevel experienceLevel,
            String bio,
            String location,
            String availability,
            String profileImageUrl,
            int profileStrength,
            boolean discoverable,
            List<SkillResponse> skills,
            List<ProjectResponse> projects
    ) {
    }

    public record ResumeResponse(UUID id, String originalFileName, long fileSize, ResumeStatus status, Instant uploadedAt, Instant parsedAt, List<SkillResponse> extractedSkills) {
    }

    public record GithubConnectRequest(
            @NotBlank String githubUserId,
            @NotBlank @Size(max = 120) String username,
            @NotBlank String accessToken
    ) {
    }

    public record GithubResponse(UUID id, String githubUserId, String username, Instant connectedAt, Instant lastSyncedAt, List<ProjectResponse> repositories) {
    }

    public record LinkedInConnectRequest(
            @NotBlank String subjectId,
            @Size(max = 200) String displayName,
            @NotBlank String accessToken,
            @NotBlank @Size(max = 500) String scope
    ) {
    }

    public record HackathonSkillResponse(String name, int importance, boolean required) {
    }

    public record HackathonTrackResponse(UUID id, String name, String description) {
    }

    public record HackathonResponse(
            UUID id,
            String name,
            String description,
            String organizer,
            Instant startDate,
            Instant endDate,
            Instant registrationDeadline,
            HackathonMode mode,
            String location,
            int teamSizeMin,
            int teamSizeMax,
            HackathonStatus status,
            String registrationUrl,
            boolean registered,
            List<HackathonSkillResponse> skills,
            List<HackathonTrackResponse> tracks
    ) {
    }

    public record HackathonMatchResponse(int matchScore, List<String> matchedSkills, List<String> missingSkills, List<String> reasons) {
    }

    public record CreateTeamRequest(
            @NotBlank @Size(max = 200) String name,
            @NotNull UUID hackathonId
    ) {
    }

    public record TeamMemberResponse(UUID userId, String fullName, String role, TeamMemberStatus status, Instant joinedAt) {
    }

    public record TeamResponse(UUID id, String name, UUID ownerId, UUID hackathonId, TeamStatus status, List<TeamMemberResponse> members) {
    }

    public record InvitationRequest(
            @NotNull UUID receiverId,
            @Size(max = 500) String message
    ) {
    }

    public record InvitationResponse(
            UUID id,
            UUID teamId,
            String teamName,
            UUID senderId,
            UUID receiverId,
            String message,
            InvitationStatus status,
            Instant createdAt,
            Instant respondedAt,
            Instant expiresAt
    ) {
    }

    public record SkillCoverageResponse(List<String> requiredSkills, List<String> coveredSkills, List<String> missingSkills, int coveragePercentage) {
    }

    public record TeamMatchResponse(
            UUID userId,
            String fullName,
            String headline,
            int compatibilityScore,
            List<String> missingSkillCoverage,
            List<String> reasons,
            String availability
    ) {
    }

    public record ConnectionResponse(UUID id, UUID requesterId, UUID receiverId, ConnectionStatus status, Instant createdAt, Instant updatedAt) {
    }

    public record InternshipSkillResponse(String name, boolean required) {
    }

    public record InternshipResponse(
            UUID id,
            String company,
            String title,
            String description,
            String location,
            String workMode,
            Instant applicationDeadline,
            String applicationUrl,
            InternshipStatus status,
            boolean saved,
            ApplicationStatus applicationStatus,
            List<InternshipSkillResponse> skills
    ) {
    }

    public record InternshipMatchResponse(
            UUID internshipId,
            int matchScore,
            List<String> matchedSkills,
            List<String> skillGaps,
            String reason
    ) {
    }

    public record ApplicationResponse(UUID id, UUID internshipId, ApplicationStatus status, Instant appliedAt, String externalApplicationUrl, boolean externalSubmissionConfirmed) {
    }

    public record NotificationResponse(UUID id, NotificationType type, String title, String body, UUID referenceId, boolean read, Instant createdAt) {
    }

    public record PublicProfileResponse(UUID publicProfileId, String fullName, String headline, String role, String location, List<SkillResponse> skills) {
    }

    public record ErrorResponse(String code, String message, Instant timestamp, String path) {
    }
}
