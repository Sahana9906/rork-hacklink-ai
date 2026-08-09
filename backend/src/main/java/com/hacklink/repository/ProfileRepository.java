package com.hacklink.repository;

import com.hacklink.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUserId(UUID userId);
    Optional<Profile> findByPublicProfileId(UUID publicProfileId);
    java.util.List<Profile> findAllByDiscoverableTrue();
}
