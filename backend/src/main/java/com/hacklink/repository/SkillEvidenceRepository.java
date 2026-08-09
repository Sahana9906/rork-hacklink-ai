package com.hacklink.repository;

import com.hacklink.entity.SkillEvidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkillEvidenceRepository extends JpaRepository<SkillEvidence, UUID> {
    List<SkillEvidence> findAllByUserSkillId(UUID userSkillId);
}
