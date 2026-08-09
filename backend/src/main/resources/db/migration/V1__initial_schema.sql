CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE app_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES app_users(id),
    public_profile_id UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    full_name VARCHAR(160) NOT NULL,
    headline VARCHAR(240),
    role VARCHAR(120),
    experience_level VARCHAR(40),
    bio TEXT,
    location VARCHAR(160),
    availability VARCHAR(120),
    profile_image_url VARCHAR(500),
    profile_strength INTEGER NOT NULL DEFAULT 0 CHECK (profile_strength BETWEEN 0 AND 100),
    discoverable BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(120) NOT NULL UNIQUE,
    normalized_name VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE user_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id),
    confidence INTEGER NOT NULL DEFAULT 0 CHECK (confidence BETWEEN 0 AND 100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_skill UNIQUE(user_id, skill_id)
);

CREATE TABLE skill_evidence (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_skill_id UUID NOT NULL REFERENCES user_skills(id) ON DELETE CASCADE,
    source VARCHAR(20) NOT NULL,
    title VARCHAR(240) NOT NULL,
    description TEXT,
    source_reference VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    url VARCHAR(500),
    source VARCHAR(20) NOT NULL DEFAULT 'MANUAL',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE resumes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    original_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(120) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_key VARCHAR(500),
    uploaded_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    parsed_at TIMESTAMPTZ,
    status VARCHAR(30) NOT NULL DEFAULT 'UPLOADED'
);

CREATE TABLE github_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES app_users(id) ON DELETE CASCADE,
    github_user_id VARCHAR(120) NOT NULL,
    username VARCHAR(120),
    access_token_encrypted TEXT,
    connected_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_synced_at TIMESTAMPTZ
);

CREATE TABLE github_repositories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    github_account_id UUID NOT NULL REFERENCES github_accounts(id) ON DELETE CASCADE,
    external_id VARCHAR(120) NOT NULL,
    name VARCHAR(240) NOT NULL,
    description TEXT,
    url VARCHAR(500),
    primary_language VARCHAR(120),
    stars INTEGER NOT NULL DEFAULT 0,
    fork BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMPTZ,
    CONSTRAINT uq_github_repo UNIQUE(github_account_id, external_id)
);

CREATE TABLE linkedin_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES app_users(id) ON DELETE CASCADE,
    subject_id VARCHAR(200) NOT NULL,
    display_name VARCHAR(200),
    access_token_encrypted TEXT,
    connected_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_synced_at TIMESTAMPTZ
);

CREATE TABLE consents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    provider VARCHAR(40) NOT NULL,
    scope VARCHAR(500) NOT NULL,
    granted_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    revoked_at TIMESTAMPTZ
);

CREATE TABLE hackathons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(240) NOT NULL,
    description TEXT,
    organizer VARCHAR(200) NOT NULL,
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ NOT NULL,
    registration_deadline TIMESTAMPTZ NOT NULL,
    mode VARCHAR(30) NOT NULL,
    location VARCHAR(200),
    team_size_min INTEGER NOT NULL CHECK (team_size_min > 0),
    team_size_max INTEGER NOT NULL CHECK (team_size_max >= team_size_min),
    status VARCHAR(30) NOT NULL DEFAULT 'UPCOMING',
    registration_url VARCHAR(500)
);

CREATE TABLE hackathon_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hackathon_id UUID NOT NULL REFERENCES hackathons(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id),
    importance INTEGER NOT NULL DEFAULT 1 CHECK (importance BETWEEN 1 AND 5),
    required BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_hackathon_skill UNIQUE(hackathon_id, skill_id)
);

CREATE TABLE hackathon_tracks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hackathon_id UUID NOT NULL REFERENCES hackathons(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    description TEXT
);

CREATE TABLE hackathon_registrations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hackathon_id UUID NOT NULL REFERENCES hackathons(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    registered_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_hackathon_registration UNIQUE(hackathon_id, user_id)
);

CREATE TABLE teams (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    owner_id UUID NOT NULL REFERENCES app_users(id),
    hackathon_id UUID NOT NULL REFERENCES hackathons(id),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE team_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    team_id UUID NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES app_users(id),
    role VARCHAR(120),
    status VARCHAR(30) NOT NULL DEFAULT 'ACCEPTED',
    joined_at TIMESTAMPTZ,
    CONSTRAINT uq_team_member UNIQUE(team_id, user_id)
);

CREATE TABLE team_invitations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    team_id UUID NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES app_users(id),
    receiver_id UUID NOT NULL REFERENCES app_users(id),
    message VARCHAR(500),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMPTZ,
    expires_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_invitation_not_self CHECK(sender_id <> receiver_id)
);

CREATE UNIQUE INDEX uq_pending_team_invitation ON team_invitations(team_id, receiver_id) WHERE status = 'PENDING';

CREATE TABLE connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    receiver_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_connection_not_self CHECK(requester_id <> receiver_id)
);

CREATE UNIQUE INDEX uq_connection_pair ON connections(LEAST(requester_id, receiver_id), GREATEST(requester_id, receiver_id));

CREATE TABLE internships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company VARCHAR(200) NOT NULL,
    title VARCHAR(240) NOT NULL,
    description TEXT,
    location VARCHAR(200),
    work_mode VARCHAR(40),
    application_deadline TIMESTAMPTZ,
    application_url VARCHAR(500),
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE internship_skills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    internship_id UUID NOT NULL REFERENCES internships(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id),
    required BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_internship_skill UNIQUE(internship_id, skill_id)
);

CREATE TABLE internship_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    internship_id UUID NOT NULL REFERENCES internships(id),
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    applied_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    external_submission_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_internship_application UNIQUE(internship_id, user_id)
);

CREATE TABLE saved_internships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    internship_id UUID NOT NULL REFERENCES internships(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    saved_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_saved_internship UNIQUE(internship_id, user_id)
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    type VARCHAR(40) NOT NULL,
    title VARCHAR(240) NOT NULL,
    body TEXT NOT NULL,
    reference_id UUID,
    read_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_skills_user ON user_skills(user_id);
CREATE INDEX idx_skill_evidence_user_skill ON skill_evidence(user_skill_id);
CREATE INDEX idx_team_members_user_status ON team_members(user_id, status);
CREATE INDEX idx_notifications_user_created ON notifications(user_id, created_at DESC);
CREATE INDEX idx_profiles_discoverable ON profiles(discoverable);
