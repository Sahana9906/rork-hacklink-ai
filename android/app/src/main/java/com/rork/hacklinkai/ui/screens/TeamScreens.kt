package com.rork.hacklinkai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.model.TeammateMatch
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.components.Avatar
import com.rork.hacklinkai.ui.components.BottomNavigationBar
import com.rork.hacklinkai.ui.components.PrimaryButton
import com.rork.hacklinkai.ui.components.SectionHeader
import com.rork.hacklinkai.ui.components.SkillChip
import com.rork.hacklinkai.ui.components.TeammateCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamBuilderScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val connected by viewModel.connectedTeammates.collectAsStateWithLifecycle()
    var pendingInvite by remember { mutableStateOf<TeammateMatch?>(null) }
    var whyMatch by remember { mutableStateOf<TeammateMatch?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI team builder", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Outlined.Tune, contentDescription = "Team preferences") } }
            )
        },
        bottomBar = { BottomNavigationBar(navController, "teamBuilder") }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("Find your missing pieces", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Complementary skills beat identical resumes.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(22.dp)) {
                    Column(Modifier.padding(17.dp)) {
                        Text("SELECTED HACKATHON", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(Modifier.height(6.dp))
                        Text("Google GenAI Hackathon", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(13.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Avatar("SB", size = 34)
                            Spacer(Modifier.width(9.dp))
                            Column {
                                Text("Your role", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                                Text("Backend Developer", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Text("Your skills", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                        Spacer(Modifier.height(7.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("Java", "Spring Boot", "AI", "PostgreSQL").forEach { SkillChip(it, selected = true) } }
                    }
                }
            }
            item {
                SectionHeader("Your team skill coverage")
                Spacer(Modifier.height(8.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.62f)), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CoverageRow("Backend", true)
                        CoverageRow("AI", true)
                        CoverageRow("Frontend", false)
                        CoverageRow("UI / UX", false)
                        CoverageRow("ML", false)
                    }
                }
            }
            item { SectionHeader("AI recommended teammates") }
            items(MockData.teammates, key = { it.id }) { teammate ->
                TeammateCard(
                    teammate = teammate,
                    isPending = teammate.id in connected,
                    onConnect = { pendingInvite = teammate },
                    onWhyMatch = { whyMatch = teammate },
                    onView = { navController.navigate("teammateProfile") }
                )
            }
        }
    }
    pendingInvite?.let { teammate ->
        AlertDialog(
            onDismissRequest = { pendingInvite = null },
            icon = { Avatar(teammate.name.take(1), teammate.avatarColor, size = 52) },
            title = { Text("Send team invitation to ${teammate.name.substringBefore(" ")}?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text("Hackathon: Google GenAI Hackathon", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Reason: ${teammate.complement}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.connectTeammate(teammate.id); pendingInvite = null }) { Text("Send invitation", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { pendingInvite = null }) { Text("Cancel") } }
        )
    }
    whyMatch?.let { teammate ->
        AlertDialog(
            onDismissRequest = { whyMatch = null },
            title = { Text("Why this match?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("AI prioritizes skill complementarity—not identical skills.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    ScoreRow("Skill complementarity", 98)
                    ScoreRow("Hackathon requirement fit", 95)
                    ScoreRow("Experience compatibility", 91)
                    ScoreRow("Availability", 94)
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer), shape = RoundedCornerShape(14.dp)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Team compatibility", fontWeight = FontWeight.Bold)
                            Text("96%", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { whyMatch = null }) { Text("Got it") } }
        )
    }
}

@Composable
private fun CoverageRow(label: String, covered: Boolean) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(24.dp).clip(CircleShape).background(if (covered) MaterialTheme.colorScheme.tertiaryContainer else Color.White.copy(alpha = 0.65f)), contentAlignment = Alignment.Center) {
            Icon(if (covered) Icons.Outlined.Check else Icons.Outlined.Close, contentDescription = null, tint = if (covered) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
        }
        Spacer(Modifier.size(10.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.weight(1f))
        Text(if (covered) "Covered" else "Missing", color = if (covered) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable
private fun ScoreRow(label: String, score: Int) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 12.sp)
            Text("$score%", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(5.dp))
        androidx.compose.material3.LinearProgressIndicator(progress = { score / 100f }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(5.dp)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeammateProfileScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val teammate = MockData.teammates.first()
    var invited by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Developer profile", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } }) }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(24.dp)) {
                    Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Avatar("AN", teammate.avatarColor, size = 76)
                        Spacer(Modifier.height(11.dp))
                        Text(teammate.name, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                        Text(teammate.role, color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
                        Spacer(Modifier.height(14.dp))
                        Text("${teammate.compatibility}% compatible", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item {
                SectionHeader("Core skills")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { teammate.skills.forEach { SkillChip(it, selected = true) } }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(17.dp)) {
                        Text("Why connect", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(teammate.complement, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp, lineHeight = 19.sp)
                        Spacer(Modifier.height(10.dp))
                        Text("Usually available ${teammate.availability.lowercase()}", color = MaterialTheme.colorScheme.tertiary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            item {
                PrimaryButton(if (invited) "Invitation sent" else "Invite to team", onClick = { if (!invited) { viewModel.connectTeammate(teammate.id); invited = true } }, modifier = Modifier.fillMaxWidth(), icon = if (invited) Icons.Outlined.Check else Icons.Outlined.PersonAdd, enabled = !invited)
            }
        }
    }
}
