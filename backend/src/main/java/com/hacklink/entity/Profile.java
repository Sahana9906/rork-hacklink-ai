package com.hacklink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "public_profile_id", nullable = false, unique = true)
    private UUID publicProfileId;

    @Column(name = "full_name", nullable = false, length = 160)
    private String fullName;

    @Column(length = 240)
    private String headline;

    @Column(length = 120)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", length = 40)
    private ExperienceLevel experienceLevel;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 160)
    private String location;

    @Column(length = 120)
    private String availability;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "profile_strength", nullable = false)
    private int profileStrength;

    @Column(nullable = false)
    private boolean discoverable = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Profile(User user, String fullName) {
        this.user = user;
        this.fullName = fullName;
        this.publicProfileId = UUID.randomUUID();
    }
}
