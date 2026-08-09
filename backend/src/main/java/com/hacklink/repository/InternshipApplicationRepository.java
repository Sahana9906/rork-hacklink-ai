package com.hacklink.repository;

import com.hacklink.entity.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, UUID> {
    Optional<InternshipApplication> findByInternshipIdAndUserId(UUID internshipId, UUID userId);
}
