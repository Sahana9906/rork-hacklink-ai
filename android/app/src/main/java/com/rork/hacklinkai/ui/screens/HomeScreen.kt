package com.rork.hacklinkai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SouthEast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.components.Avatar
import com.rork.hacklinkai.ui.components.BottomNavigationBar
import com.rork.hacklinkai.ui.components.DeadlineCard
import com.rork.hacklinkai.ui.components.HackathonCard
import com.rork.hacklinkai.ui.components.InternshipCard
import com.rork.hacklinkai.ui.components.ProfileStrengthCard
import com.rork.hacklinkai.ui.components.SectionHeader
import com.rork.hacklinkai.ui.components.TeammateCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val connected by viewModel.connectedTeammates.collectAsStateWithLifecycle()
    val saved by viewModel.savedInternships.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar("SB", size = 38)
                        Spacer(Modifier.padding(horizontal = 5.dp))
                        Column {
                            Text("Good morning, Sahana", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Your build season is on.", fontSize = 11.sp, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController, "home") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column {
                    Text("Your developer edge", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(5.dp))
                    Text("AI-powered paths from skills to opportunities.", color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            }
            item { ProfileStrengthCard(strength = profile.profileStrength, onClick = { navController.navigate("profile") }) }
            item {
                SectionHeader("Recommended hackathons", "View all", onAction = { navController.navigate("hackathons") })
            }
            item {
                HackathonCard(MockData.hackathons.first(), { navController.navigate("hackathonDetail") }, compact = true)
            }
            item {
                SectionHeader("AI team matches", "Build a team", onAction = { navController.navigate("teamBuilder") })
            }
            item {
                TeammateCard(
                    teammate = MockData.teammates.first(),
                    isPending = MockData.teammates.first().id in connected,
                    onConnect = { navController.navigate("teamBuilder") },
                    onWhyMatch = { navController.navigate("teamBuilder") },
                    onView = { navController.navigate("teammateProfile") }
                )
            }
            item {
                SectionHeader("Internship matches", "View all", onAction = { navController.navigate("internships") })
            }
            item {
                InternshipCard(
                    internship = MockData.internships.first(),
                    isSaved = MockData.internships.first().id in saved,
                    onClick = { navController.navigate("internshipDetail") },
                    onSave = { viewModel.toggleSavedInternship(MockData.internships.first().id) },
                    compact = true
                )
            }
            item { SectionHeader("Upcoming deadlines") }
            item {
                DeadlineCard("Submission", "Google GenAI Hackathon", "2 days left")
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.SouthEast, contentDescription = null, tint = androidx.compose.material3.MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(end = 6.dp))
                    Text("Your next best action: invite a frontend teammate.", color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            }
        }
    }
}
