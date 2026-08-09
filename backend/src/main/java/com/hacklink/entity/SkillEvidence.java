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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "skill_evidence")
@Getter
@Setter
@NoArgsConstructor
public class SkillEvidence {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_skill_id", nullable = false)
    private UserSkill userSkill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkillSource source;

    @Column(nullable = false, length = 240)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "source_reference", length = 500)
    private String sourceReference;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public SkillEvidence(UserSkill userSkill, SkillSource source, String title, String description, String sourceReference) {
        this.userSkill = userSkill;
        this.source = source;
        this.title = title;
        this.description = description;
        this.sourceReference = sourceReference;
    }
}
