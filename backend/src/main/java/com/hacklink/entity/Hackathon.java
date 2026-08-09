package com.hacklink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "hackathons")
@Getter
@Setter
@NoArgsConstructor
public class Hackathon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 240)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 200)
    private String organizer;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "registration_deadline", nullable = false)
    private Instant registrationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HackathonMode mode;

    @Column(length = 200)
    private String location;

    @Column(name = "team_size_min", nullable = false)
    private int teamSizeMin;

    @Column(name = "team_size_max", nullable = false)
    private int teamSizeMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HackathonStatus status = HackathonStatus.UPCOMING;

    @Column(name = "registration_url", length = 500)
    private String registrationUrl;
}
