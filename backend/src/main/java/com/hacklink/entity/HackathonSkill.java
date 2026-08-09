package com.hacklink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "hackathon_skills", uniqueConstraints = @UniqueConstraint(name = "uq_hackathon_skill", columnNames = {"hackathon_id", "skill_id"}))
@Getter
@Setter
@NoArgsConstructor
public class HackathonSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private int importance = 1;

    @Column(nullable = false)
    private boolean required;

    public HackathonSkill(Hackathon hackathon, Skill skill, int importance, boolean required) {
        this.hackathon = hackathon;
        this.skill = skill;
        this.importance = importance;
        this.required = required;
    }
}
