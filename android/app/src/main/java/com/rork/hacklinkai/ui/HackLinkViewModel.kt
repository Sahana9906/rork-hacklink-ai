package com.rork.hacklinkai.ui

import androidx.lifecycle.ViewModel
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HackLinkViewModel : ViewModel() {
    private val _profile = MutableStateFlow(MockData.user)
    val profile: StateFlow<User> = _profile.asStateFlow()

    private val _connectedTeammates = MutableStateFlow<Set<String>>(emptySet())
    val connectedTeammates: StateFlow<Set<String>> = _connectedTeammates.asStateFlow()

    private val _savedInternships = MutableStateFlow<Set<String>>(emptySet())
    val savedInternships: StateFlow<Set<String>> = _savedInternships.asStateFlow()

    private val _joinedHackathon = MutableStateFlow(false)
    val joinedHackathon: StateFlow<Boolean> = _joinedHackathon.asStateFlow()

    fun connectGithub() {
        _profile.update { it.copy(githubConnected = true, profileStrength = maxOf(it.profileStrength, 92)) }
    }

    fun uploadResume() {
        _profile.update { it.copy(resumeUploaded = true, profileStrength = maxOf(it.profileStrength, 94)) }
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
}
