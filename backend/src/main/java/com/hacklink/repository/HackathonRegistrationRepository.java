package com.hacklink.repository;

import com.hacklink.entity.HackathonRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HackathonRegistrationRepository extends JpaRepository<HackathonRegistration, UUID> {
    Optional<HackathonRegistration> findByHackathonIdAndUserId(UUID hackathonId, UUID userId);
}
