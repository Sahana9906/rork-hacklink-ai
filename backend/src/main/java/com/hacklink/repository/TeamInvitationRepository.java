package com.hacklink.repository;

import com.hacklink.entity.InvitationStatus;
import com.hacklink.entity.TeamInvitation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, UUID> {
    List<TeamInvitation> findAllByReceiverIdAndStatusOrderByCreatedAtDesc(UUID receiverId, InvitationStatus status);
    List<TeamInvitation> findAllByTeamIdAndStatus(UUID teamId, InvitationStatus status);
    boolean existsByTeamIdAndReceiverIdAndStatus(UUID teamId, UUID receiverId, InvitationStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from TeamInvitation i where i.id = :id")
    Optional<TeamInvitation> findByIdForUpdate(@Param("id") UUID id);
}
