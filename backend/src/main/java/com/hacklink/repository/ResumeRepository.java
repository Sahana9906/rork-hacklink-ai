package com.hacklink.repository;

import com.hacklink.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    Optional<Resume> findTopByUserIdOrderByUploadedAtDesc(UUID userId);
}
