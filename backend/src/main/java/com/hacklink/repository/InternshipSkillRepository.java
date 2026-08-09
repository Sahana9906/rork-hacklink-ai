package com.hacklink.repository;

import com.hacklink.entity.InternshipSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InternshipSkillRepository extends JpaRepository<InternshipSkill, UUID> {
    List<InternshipSkill> findAllByInternshipId(UUID internshipId);
}
