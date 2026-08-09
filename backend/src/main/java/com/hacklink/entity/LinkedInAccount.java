package com.hacklink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "linkedin_accounts")
@Getter
@Setter
@NoArgsConstructor
public class LinkedInAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "subject_id", nullable = false, length = 200)
    private String subjectId;

    @Column(name = "display_name", length = 200)
    private String displayName;

    @Column(name = "access_token_encrypted", columnDefinition = "TEXT")
    private String accessTokenEncrypted;

    @Column(name = "connected_at", nullable = false)
    private Instant connectedAt = Instant.now();

    @Column(name = "last_synced_at")
    private Instant lastSyncedAt;

    public LinkedInAccount(User user, String subjectId, String displayName, String accessTokenEncrypted) {
        this.user = user;
        this.subjectId = subjectId;
        this.displayName = displayName;
        this.accessTokenEncrypted = accessTokenEncrypted;
    }
}
