package com.hacklink.repository;

import com.hacklink.entity.GithubAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GithubAccountRepository extends JpaRepository<GithubAccount, UUID> {
    Optional<GithubAccount> findByUserId(UUID userId);
}
