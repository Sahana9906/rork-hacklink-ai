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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "team_members", uniqueConstraints = @UniqueConstraint(name = "uq_team_member", columnNames = {"team_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 120)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TeamMemberStatus status = TeamMemberStatus.ACCEPTED;

    @Column(name = "joined_at")
    private Instant joinedAt;

    public TeamMember(Team team, User user, String role, TeamMemberStatus status) {
        this.team = team;
        this.user = user;
        this.role = role;
        this.status = status;
        this.joinedAt = status == TeamMemberStatus.ACCEPTED ? Instant.now() : null;
    }
}
