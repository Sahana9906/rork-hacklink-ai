package com.hacklink.repository;

import com.hacklink.entity.LinkedInAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkedInAccountRepository extends JpaRepository<LinkedInAccount, UUID> {
    Optional<LinkedInAccount> findByUserId(UUID userId);
}
