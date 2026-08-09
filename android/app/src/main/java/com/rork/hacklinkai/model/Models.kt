package com.rork.hacklinkai.model

data class Skill(
    val name: String,
    val confidence: Int,
    val evidence: String
)

data class Project(
    val name: String,
    val summary: String,
    val technologies: List<String>
)

data class Achievement(
    val title: String,
    val subtitle: String
)

data class User(
    val name: String,
    val role: String,
    val experienceLevel: String,
    val interests: List<String>,
    val availability: String,
    val profileStrength: Int,
    val skills: List<Skill>,
    val projects: List<Project>,
    val hackathonsJoined: Int,
    val connections: Int,
    val headline: String = "Backend Developer building useful AI tools",
    val location: String = "Bengaluru, India",
    val bio: String = "I enjoy turning complex backend systems into simple, useful products.",
    val discoverable: Boolean = true,
    val githubConnected: Boolean = false,
    val linkedinConnected: Boolean = false,
    val resumeUploaded: Boolean = false
)

data class Hackathon(
    val id: String,
    val name: String,
    val match: Int,
    val category: String,
    val mode: String,
    val duration: String,
    val date: String,
    val teamSize: String,
    val requiredSkills: List<String>,
    val tracks: List<String>,
    val matchingReasons: List<String>,
    val skillGap: String,
    val accentHex: Long
)

data class TeamMember(
    val name: String,
    val role: String,
    val skills: List<String>
)

data class Team(
    val name: String,
    val hackathon: String,
    val members: List<TeamMember>,
    val status: String = "RECRUITING",
    val maxMembers: Int = 5,
    val coverage: Map<String, Boolean> = emptyMap()
)

data class TeammateMatch(
    val id: String,
    val name: String,
    val role: String,
    val skills: List<String>,
    val compatibility: Int,
    val avatarColor: Long,
    val complement: String,
    val availability: String
)

data class Internship(
    val id: String,
    val company: String,
    val role: String,
    val match: Int,
    val location: String,
    val mode: String,
    val duration: String,
    val requiredSkills: List<String>,
    val skillGap: String,
    val accentHex: Long
)

data class Notification(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: String,
    val type: NotificationType,
    val isUnread: Boolean = true
)

enum class NotificationType {
    DEADLINE,
    TEAM,
    OPPORTUNITY
}

data class ConnectionProfile(
    val id: String,
    val name: String,
    val role: String,
    val skills: List<String>,
    val status: String = "Connect",
    val avatarColor: Long = 0xFF5B4CF6
)

data class TeamInvitation(
    val id: String,
    val sender: String,
    val teamName: String,
    val hackathon: String,
    val role: String,
    val reason: List<String>,
    val status: String = "Pending"
)
