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
@Table(name = "internships")
@Getter
@Setter
@NoArgsConstructor
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String company;

    @Column(nullable = false, length = 240)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String location;

    @Column(name = "work_mode", length = 40)
    private String workMode;

    @Column(name = "application_deadline")
    private Instant applicationDeadline;

    @Column(name = "application_url", length = 500)
    private String applicationUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InternshipStatus status = InternshipStatus.OPEN;
}
