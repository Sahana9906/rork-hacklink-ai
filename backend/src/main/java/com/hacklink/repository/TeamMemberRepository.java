package com.hacklink.repository;

import com.hacklink.entity.TeamMember;
import com.hacklink.entity.TeamMemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
    List<TeamMember> findAllByTeamId(UUID teamId);
    Optional<TeamMember> findByTeamIdAndUserId(UUID teamId, UUID userId);
    boolean existsByTeamIdAndUserId(UUID teamId, UUID userId);
    List<TeamMember> findAllByUserIdAndStatusIn(UUID userId, Collection<TeamMemberStatus> statuses);
    long countByTeamIdAndStatus(UUID teamId, TeamMemberStatus status);
}
