package com.hacklink.repository;

import com.hacklink.entity.GithubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GithubRepositoryRepository extends JpaRepository<GithubRepository, UUID> {
    List<GithubRepository> findAllByGithubAccountId(UUID githubAccountId);
    Optional<GithubRepository> findByGithubAccountIdAndExternalId(UUID githubAccountId, String externalId);
}
