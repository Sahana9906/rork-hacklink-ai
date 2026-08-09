package com.hacklink.repository;

import com.hacklink.entity.HackathonTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HackathonTrackRepository extends JpaRepository<HackathonTrack, UUID> {
    List<HackathonTrack> findAllByHackathonId(UUID hackathonId);
}
