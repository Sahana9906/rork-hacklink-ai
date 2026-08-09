package com.hacklink.repository;

import com.hacklink.entity.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HackathonRepository extends JpaRepository<Hackathon, UUID> {
    List<Hackathon> findAllByOrderByRegistrationDeadlineAsc();
}
