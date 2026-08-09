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

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "github_repositories", uniqueConstraints = @UniqueConstraint(name = "uq_github_repo", columnNames = {"github_account_id", "external_id"}))
@Getter
@Setter
@NoArgsConstructor
public class GithubRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "github_account_id", nullable = false)
    private GithubAccount githubAccount;

    @Column(name = "external_id", nullable = false, length = 120)
    private String externalId;

    @Column(nullable = false, length = 240)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String url;

    @Column(name = "primary_language", length = 120)
    private String primaryLanguage;

    @Column(nullable = false)
    private int stars;

    @Column(nullable = false)
    private boolean fork;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public GithubRepository(GithubAccount account, String externalId, String name) {
        this.githubAccount = account;
        this.externalId = externalId;
        this.name = name;
    }
}
