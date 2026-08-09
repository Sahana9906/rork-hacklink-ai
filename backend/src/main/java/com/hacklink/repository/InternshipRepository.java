package com.hacklink.repository;

import com.hacklink.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InternshipRepository extends JpaRepository<Internship, UUID> {
    List<Internship> findAllByOrderByApplicationDeadlineAsc();
}
