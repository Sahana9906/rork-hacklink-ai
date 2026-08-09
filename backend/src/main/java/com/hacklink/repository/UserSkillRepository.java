package com.hacklink.repository;

import com.hacklink.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSkillRepository extends JpaRepository<UserSkill, UUID> {
    List<UserSkill> findAllByUserId(UUID userId);
    Optional<UserSkill> findByUserIdAndSkillId(UUID userId, UUID skillId);
}
