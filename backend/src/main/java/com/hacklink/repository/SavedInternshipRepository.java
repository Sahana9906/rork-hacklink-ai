package com.hacklink.repository;

import com.hacklink.entity.SavedInternship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SavedInternshipRepository extends JpaRepository<SavedInternship, UUID> {
    Optional<SavedInternship> findByInternshipIdAndUserId(UUID internshipId, UUID userId);
}
