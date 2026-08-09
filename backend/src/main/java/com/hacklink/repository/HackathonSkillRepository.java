package com.hacklink.repository;

import com.hacklink.entity.HackathonSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HackathonSkillRepository extends JpaRepository<HackathonSkill, UUID> {
    List<HackathonSkill> findAllByHackathonId(UUID hackathonId);
    List<HackathonSkill> findAllByHackathonIdAndRequiredTrue(UUID hackathonId);
}
