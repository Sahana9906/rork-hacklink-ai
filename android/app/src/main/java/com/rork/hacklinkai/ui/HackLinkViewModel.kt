package com.rork.hacklinkai.ui

import androidx.lifecycle.ViewModel
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.model.Notification
import com.rork.hacklinkai.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Holds the prototype's shared UI state so mock interactions behave like a real product.
 * Backend repositories can replace these methods without changing the Compose screens.
 */
class HackLinkViewModel : ViewModel() {
    private val _profile = MutableStateFlow(MockData.user)
    val profile: StateFlow<User> = _profile.asStateFlow()

    private val _connectedTeammates = MutableStateFlow<Set<String>>(emptySet())
    val connectedTeammates: StateFlow<Set<String>> = _connectedTeammates.asStateFlow()

    private val _savedInternships = MutableStateFlow<Set<String>>(emptySet())
    val savedInternships: StateFlow<Set<String>> = _savedInternships.asStateFlow()

    private val _joinedHackathon = MutableStateFlow(false)
    val joinedHackathon: StateFlow<Boolean> = _joinedHackathon.asStateFlow()

    private val _teamFinalized = MutableStateFlow(false)
    val teamFinalized: StateFlow<Boolean> = _teamFinalized.asStateFlow()

    private val _invitationStatus = MutableStateFlow("Pending")
    val invitationStatus: StateFlow<String> = _invitationStatus.asStateFlow()

    private val _connectionStatuses = MutableStateFlow(
        MockData.connections.associate { connection -> connection.id to connection.status }
    )
    val connectionStatuses: StateFlow<Map<String, String>> = _connectionStatuses.asStateFlow()

    private val _notifications = MutableStateFlow(MockData.notifications)
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _hasCompletedOnboarding = MutableStateFlow(false)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    fun completeOnboarding() {
        _hasCompletedOnboarding.value = true
    }

    fun connectGithub() {
        _profile.update { it.copy(githubConnected = true, profileStrength = maxOf(it.profileStrength, 92)) }
    }

    fun connectLinkedin() {
        _profile.update { it.copy(linkedinConnected = true, profileStrength = maxOf(it.profileStrength, 94)) }
    }

    fun uploadResume() {
        _profile.update { it.copy(resumeUploaded = true, profileStrength = maxOf(it.profileStrength, 96)) }
    }

    fun updateProfile(
        name: String,
        headline: String,
        role: String,
        experienceLevel: String,
        location: String,
        bio: String,
        interests: List<String>,
        availability: String
    ) {
        _profile.update {
            it.copy(
                name = name.ifBlank { it.name },
                headline = headline.ifBlank { it.headline },
                role = role.ifBlank { it.role },
                experienceLevel = experienceLevel.ifBlank { it.experienceLevel },
                location = location.ifBlank { it.location },
                bio = bio.ifBlank { it.bio },
                interests = interests,
                availability = availability,
                profileStrength = maxOf(it.profileStrength, 72)
            )
        }
    }

    fun setDiscoverable(discoverable: Boolean) {
        _profile.update { it.copy(discoverable = discoverable) }
    }

    fun connectTeammate(id: String) {
        _connectedTeammates.update { it + id }
    }

    fun toggleSavedInternship(id: String) {
        _savedInternships.update { current ->
            if (id in current) current - id else current + id
        }
    }

    fun joinHackathon() {
        _joinedHackathon.value = true
    }

    fun finalizeTeam() {
        _teamFinalized.value = true
    }

    fun acceptInvitation() {
        _invitationStatus.value = "Accepted"
    }

    fun rejectInvitation() {
        _invitationStatus.value = "Declined"
    }

    fun sendConnection(id: String) {
        _connectionStatuses.update { current -> current + (id to "Pending") }
    }

    fun acceptConnection(id: String) {
        _connectionStatuses.update { current -> current + (id to "Connected") }
    }

    fun rejectConnection(id: String) {
        _connectionStatuses.update { current -> current + (id to "Rejected") }
    }

    fun markNotificationRead(id: String) {
        _notifications.update { notifications ->
            notifications.map { notification ->
                if (notification.id == id) notification.copy(isUnread = false) else notification
            }
        }
    }

    fun markAllNotificationsRead() {
        _notifications.update { notifications -> notifications.map { it.copy(isUnread = false) } }
    }
}
