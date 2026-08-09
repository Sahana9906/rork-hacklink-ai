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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.model.ConnectionProfile
import com.rork.hacklinkai.model.NotificationType
import com.rork.hacklinkai.model.Team
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.components.Avatar
import com.rork.hacklinkai.ui.components.BottomNavigationBar
import com.rork.hacklinkai.ui.components.OutlineButton
import com.rork.hacklinkai.ui.components.PrimaryButton
import com.rork.hacklinkai.ui.components.SectionHeader
import com.rork.hacklinkai.ui.components.SkillChip
import kotlinx.coroutines.delay

@Composable
fun ProfileSetupScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf(profile.name) }
    var headline by remember { mutableStateOf(profile.headline) }
    var role by remember { mutableStateOf(profile.role) }
    var level by remember { mutableStateOf(profile.experienceLevel) }
    var location by remember { mutableStateOf(profile.location) }
    var bio by remember { mutableStateOf(profile.bio) }
    var availability by remember { mutableStateOf(profile.availability) }
    var interests by remember { mutableStateOf(profile.interests) }
    val roles = listOf("Backend Developer", "Frontend Developer", "Full Stack Developer", "AI/ML Engineer", "Android Developer", "UI/UX Designer", "Cloud/DevOps", "Cybersecurity", "Product/Business", "Domain Expert")
    val interestOptions = listOf("AI", "Cloud", "FinTech", "Healthcare", "Cybersecurity", "Web", "Mobile", "Open Source")
    val availabilityOptions = listOf("Weekdays", "Weekends", "Evenings")

    Scaffold(topBar = { SimpleTopBar("Profile setup", navController, showBack = false) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text("Tell us about your developer journey", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("This helps HackLink understand what you can bring to a team.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item { SetupField("Full name", name) { name = it } }
            item { SetupField("Headline", headline) { headline = it } }
            item {
                ChoiceSection("Role") {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(roles) { option ->
                            FilterChip(selected = role == option, onClick = { role = option }, label = { Text(option, fontSize = 12.sp) }, colors = setupChipColors())
                        }
                    }
                }
            }
            item {
                ChoiceSection("Experience level") {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(listOf("Student", "Beginner", "Intermediate", "Advanced")) { option ->
                            FilterChip(selected = level == option, onClick = { level = option }, label = { Text(option) }, colors = setupChipColors())
                        }
                    }
                }
            }
            item { SetupField("Location", location) { location = it } }
            item { SetupField("Bio", bio, minLines = 3) { bio = it } }
            item {
                ChoiceSection("Interests") {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(interestOptions) { option ->
                            FilterChip(selected = option in interests, onClick = { interests = interests.toggle(option) }, label = { Text(option) }, colors = setupChipColors())
                        }
                    }
                }
            }
            item {
                ChoiceSection("Availability") {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availabilityOptions) { option ->
                            FilterChip(selected = availability == option, onClick = { availability = option }, label = { Text(option) }, colors = setupChipColors())
                        }
                    }
                }
            }
            item {
                PrimaryButton("Continue", onClick = {
                    viewModel.updateProfile(name, headline, role, level, location, bio, interests, availability)
                    navController.navigate("connectSources")
                }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun ConnectSourcesScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    Scaffold(topBar = { SimpleTopBar("Connect your sources", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text("Make your profile more complete", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Only information you choose to share is used in your profile.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item {
                SourceCard("GitHub", "Analyze your repositories, languages and projects.", Icons.Outlined.Code, profile.githubConnected, "Connected ✓") { viewModel.connectGithub() }
            }
            item {
                SourceCard("LinkedIn", "Import permitted professional profile information.", Icons.Outlined.Link, profile.linkedinConnected, "Connected ✓") { viewModel.connectLinkedin() }
            }
            item {
                SourceCard("Resume", "Extract skills, projects and experience from your resume.", Icons.Outlined.UploadFile, profile.resumeUploaded, "Uploaded ✓") { viewModel.uploadResume() }
            }
            item {
                PrimaryButton("Continue", onClick = { navController.navigate("reviewImported") }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun ReviewImportedDataScreen(navController: NavController) {
    Scaffold(topBar = { SimpleTopBar("Review imported data", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text("Review before we analyze", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("You are always in control of the information in your profile.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item { ImportedCard("GitHub", "18 repositories · 12 relevant projects", listOf("Java", "Spring Boot", "Python", "React", "PostgreSQL"), Icons.Outlined.Code) }
            item { ImportedCard("LinkedIn", "Backend Developer", listOf("Professional headline", "Experience level"), Icons.Outlined.Link) }
            item { ImportedCard("Resume", "8 skills · 4 projects · 2 certifications", listOf("Skills", "Projects", "Certifications"), Icons.Outlined.UploadFile) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlineButton("Edit", onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f), icon = Icons.Outlined.Edit)
                    PrimaryButton("Continue", onClick = { navController.navigate("profileAnalysis") }, modifier = Modifier.weight(1f), icon = Icons.Outlined.AutoAwesome)
                }
            }
        }
    }
}

@Composable
fun AiProfileAnalysisScreen(navController: NavController) {
    var stage by remember { mutableIntStateOf(0) }
    val stages = listOf("GitHub analyzed", "Resume analyzed", "Profile information processed", "Normalizing skills", "Calculating profile strength", "Preparing recommendations")
    LaunchedEffect(Unit) {
        for (index in 0 until stages.size) {
            delay(500)
            stage = index + 1
        }
    }
    val ready = stage >= stages.size
    Scaffold(topBar = { SimpleTopBar("AI profile analysis", navController, showBack = false) }) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(42.dp))
            Box(Modifier.size(88.dp).clip(RoundedCornerShape(28.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(42.dp))
            }
            Spacer(Modifier.height(24.dp))
            Text(if (ready) "Your profile is ready!" else "Building your developer profile...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(if (ready) "We turned your evidence into a profile you can use to find better teams and opportunities." else "HackLink is organizing your skills and evidence.", color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, fontSize = 13.sp, lineHeight = 20.sp)
            Spacer(Modifier.height(28.dp))
            LinearProgressIndicator(progress = { stage / stages.size.toFloat() }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp)))
            Spacer(Modifier.height(24.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                    stages.forEachIndexed { index, label ->
                        AnalysisRow(label, index < stage, index == stage && !ready)
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            if (ready) {
                PrimaryButton("View my profile", onClick = { navController.navigate("profile") }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SkillEvidenceScreen(navController: NavController) {
    val skill = MockData.user.skills.firstOrNull { it.name == "Spring Boot" } ?: MockData.user.skills.first()
    Scaffold(topBar = { SimpleTopBar("Skill evidence", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("IDENTIFIED SKILL", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(skill.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                            Text("${skill.confidence}%", color = MaterialTheme.colorScheme.primary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Text("Confidence from multiple sources", color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
                    }
                }
            }
            item { SectionHeader("Sources") }
            item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SkillChip("GitHub", selected = true); SkillChip("Resume", selected = true) } }
            item { SectionHeader("Evidence") }
            item { EvidenceCard("backend-api repository", "GitHub", "Spring Boot REST API project with controllers, services and persistence.") }
            item { EvidenceCard("trading-app repository", "GitHub", "Uses Spring Boot for account, portfolio and transaction services.") }
            item { EvidenceCard("Resume project experience", "Resume", "Spring Boot listed across two shipped backend projects.") }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                    Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.width(10.dp))
                        Text("Evidence-backed skills make your matches more explainable.", color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TeamsScreen(navController: NavController) {
    Scaffold(topBar = { SimpleTopBar("Teams", navController, showBack = false) }, bottomBar = { BottomNavigationBar(navController, "teams") }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("Build with the right people", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Your teams are organized around complementary skill coverage.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item { SectionHeader("My teams") }
            items(MockData.teams, key = { it.name }) { team -> TeamSummaryCard(team) { navController.navigate("teamDetails") } }
            item { SectionHeader("Pending invitations") }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) { Icon(Icons.Outlined.PersonAdd, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) { Text("1 invitation waiting", fontWeight = FontWeight.Bold); Text("From Rahul · Impact Builders", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) }
                        TextButton(onClick = { navController.navigate("invitation") }) { Text("View") }
                    }
                }
            }
            item { PrimaryButton("Find a team", onClick = { navController.navigate("teamBuilder") }, modifier = Modifier.fillMaxWidth(), icon = Icons.Outlined.Groups) }
        }
    }
}

@Composable
fun TeamDetailsScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val finalized by viewModel.teamFinalized.collectAsStateWithLifecycle()
    var showFinalize by remember { mutableStateOf(false) }
    val team = MockData.teams.first()
    val coverage = team.coverage
    Scaffold(topBar = { SimpleTopBar("Team details", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text(team.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text(team.hackathon, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) { StatusPill(if (finalized) "FINALIZED" else team.status, finalized); Spacer(Modifier.width(8.dp)); Text("${team.members.size} / ${team.maxMembers} members", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) }
            }
            item { SectionHeader("Members") }
            items(team.members, key = { it.name }) { member ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Avatar(member.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""), size = 44)
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) { Text(member.name, fontWeight = FontWeight.Bold); Text(member.role, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp); Text(member.skills.joinToString(" · "), color = MaterialTheme.colorScheme.primary, fontSize = 11.sp) }
                        if (member.role.contains("OWNER")) Text("OWNER", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { SectionHeader("Team skill coverage") }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                        coverage.forEach { (skill, covered) -> CoverageLine(skill, covered) }
                        HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Coverage", fontWeight = FontWeight.Bold); Text("${coverage.values.count { it } * 100 / coverage.size}%", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold) }
                    }
                }
            }
            item {
                PrimaryButton(if (finalized) "Team finalized" else "Finalize team", onClick = { showFinalize = true }, modifier = Modifier.fillMaxWidth(), enabled = !finalized)
            }
        }
    }
    if (showFinalize) {
        AlertDialog(onDismissRequest = { showFinalize = false }, title = { Text("Finalize this team?") }, text = { Text("Team membership will be locked after finalization.") }, confirmButton = { TextButton(onClick = { viewModel.finalizeTeam(); showFinalize = false }) { Text("Finalize", fontWeight = FontWeight.Bold) } }, dismissButton = { TextButton(onClick = { showFinalize = false }) { Text("Cancel") } })
    }
}

@Composable
fun InvitationScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val status by viewModel.invitationStatus.collectAsStateWithLifecycle()
    val invitation = MockData.invitations.first()
    Scaffold(topBar = { SimpleTopBar("Team invitation", navController) }) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(20.dp)) {
                    Text("YOU'RE INVITED", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("${invitation.sender} invited you to", color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 13.sp)
                    Text(invitation.teamName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(5.dp))
                    Text(invitation.hackathon, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(18.dp))
            Text("Your role", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Text(invitation.role, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Spacer(Modifier.height(20.dp))
            SectionHeader("Why you were selected")
            Spacer(Modifier.height(9.dp))
            invitation.reason.forEach { reason ->
                Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text(reason, fontSize = 13.sp) }
            }
            Spacer(Modifier.weight(1f))
            if (status == "Pending") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlineButton("Decline", onClick = { viewModel.rejectInvitation() }, modifier = Modifier.weight(1f), icon = Icons.Outlined.Close)
                    PrimaryButton("Accept invitation", onClick = { viewModel.acceptInvitation() }, modifier = Modifier.weight(1.3f), icon = Icons.Outlined.Check)
                }
            } else {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = if (status == "Accepted") MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(if (status == "Accepted") Icons.Outlined.Check else Icons.Outlined.Close, contentDescription = null); Spacer(Modifier.width(10.dp)); Text(if (status == "Accepted") "You're now part of ${invitation.teamName}." else "Invitation declined.", fontWeight = FontWeight.Bold) }
                }
                Spacer(Modifier.height(12.dp))
                PrimaryButton("Back to teams", onClick = { navController.navigate("teams") { popUpTo("teams") { inclusive = true } } }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun ConnectionsScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val statuses by viewModel.connectionStatuses.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Requests", "Sent")
    val visible = MockData.connections.filter { connection ->
        when (selectedTab) {
            "Requests" -> statuses[connection.id] == "Accept"
            "Sent" -> statuses[connection.id] == "Pending"
            else -> true
        }
    }
    Scaffold(topBar = { SimpleTopBar("Connections", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Your developer network", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Connect with people you could build with next.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(tabs) { tab -> FilterChip(selected = selectedTab == tab, onClick = { selectedTab = tab }, label = { Text(tab) }, colors = setupChipColors()) } }
            }
            items(visible, key = { it.id }) { connection -> ConnectionCard(connection, statuses[connection.id] ?: connection.status, viewModel) }
            if (visible.isEmpty()) item { com.rork.hacklinkai.ui.components.EmptyState("No connections here", "Your next teammate could be one conversation away.") }
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    Scaffold(topBar = { SimpleTopBar("Settings", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text("Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold); Spacer(Modifier.height(5.dp)); Text("Manage your account and how your profile is shared.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp); Spacer(Modifier.height(12.dp)) }
            item { SettingsRow("Account", "Email, password and account preferences", Icons.Outlined.PersonAdd) }
            item { SettingsRow("Notifications", "Deadlines, team updates and opportunities", Icons.Outlined.AutoAwesome) }
            item { SettingsRow("Connected accounts", "GitHub, LinkedIn and resume", Icons.Outlined.Link) }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Public, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) { Text("Profile discoverability", fontWeight = FontWeight.Bold); Text("Allow other developers to discover you for hackathon teams.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 17.sp) }
                        Switch(checked = profile.discoverable, onCheckedChange = viewModel::setDiscoverable)
                    }
                }
            }
            item { SettingsRow("Privacy", "Your QR only contains a public profile identifier", Icons.Outlined.QrCode2) }
            item { Spacer(Modifier.height(14.dp)); OutlinedButton(onClick = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(14.dp)) { Text("Log out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) } }
        }
    }
}

@Composable
fun PublicProfileScreen(navController: NavController, viewModel: HackLinkViewModel) {
    var connected by remember { mutableStateOf(false) }
    Scaffold(topBar = { SimpleTopBar("Public developer profile", navController) }) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(18.dp))
            Avatar("SB", size = 84)
            Spacer(Modifier.height(13.dp))
            Text("Sahana B", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            Text("Backend Developer", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            Spacer(Modifier.height(22.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Skills", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(9.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { listOf("Java", "Spring Boot", "AI").forEach { SkillChip(it, selected = true) } }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("This is a safe public profile preview. Private contact details are hidden.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Spacer(Modifier.weight(1f))
            PrimaryButton(if (connected) "Connected ✓" else "Connect", onClick = { connected = true }, modifier = Modifier.fillMaxWidth(), icon = if (connected) Icons.Outlined.Check else Icons.Outlined.PersonAdd, enabled = !connected)
        }
    }
}

@Composable
private fun SourceCard(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, connected: Boolean, connectedLabel: String, onConnect: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(17.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(Modifier.size(44.dp).clip(RoundedCornerShape(13.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp); Spacer(Modifier.height(4.dp)); Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 18.sp) }
            }
            Spacer(Modifier.height(13.dp))
            if (connected) Text(connectedLabel, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            else OutlinedButton(onClick = onConnect, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(13.dp)) { Text(if (title == "Resume") "Upload resume" else "Connect $title") }
        }
    }
}

@Composable
private fun ImportedCard(title: String, summary: String, tags: List<String>, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(9.dp)); Text(title, fontWeight = FontWeight.Bold); Spacer(Modifier.weight(1f)); Text("Edit", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(8.dp)); Text(summary, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            Spacer(Modifier.height(10.dp)); Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { tags.forEach { SkillChip(it) } }
        }
    }
}

@Composable
private fun AnalysisRow(label: String, completed: Boolean, active: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(23.dp).clip(CircleShape).background(if (completed) MaterialTheme.colorScheme.tertiaryContainer else if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            if (completed) Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(15.dp)) else Text(if (active) "•" else "○", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp)); Text(label, color = if (completed) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = if (completed) FontWeight.SemiBold else FontWeight.Normal, fontSize = 13.sp)
    }
}

@Composable
private fun EvidenceCard(title: String, source: String, description: String) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(15.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(title, fontWeight = FontWeight.Bold); Text(source, color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold) }; Spacer(Modifier.height(6.dp)); Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 18.sp) }
    }
}

@Composable
private fun TeamSummaryCard(team: Team, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) { Column(Modifier.weight(1f)) { Text(team.name, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp); Text(team.hackathon, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) }; StatusPill(team.status, false) }
            Spacer(Modifier.height(12.dp)); Text("${team.members.size} / ${team.maxMembers} members", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp); Spacer(Modifier.height(8.dp)); LinearProgressIndicator(progress = { team.coverage.values.count { it }.toFloat() / team.coverage.size }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(6.dp))); Spacer(Modifier.height(7.dp)); Text("${team.coverage.values.count { it } * 100 / team.coverage.size}% skill coverage", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatusPill(label: String, success: Boolean) {
    Box(Modifier.clip(RoundedCornerShape(9.dp)).background(if (success || label == "RECRUITING") MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer).padding(horizontal = 9.dp, vertical = 6.dp)) { Text(label, color = if (success || label == "RECRUITING") MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun CoverageLine(label: String, covered: Boolean) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(23.dp).clip(CircleShape).background(if (covered) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) { Icon(if (covered) Icons.Outlined.Check else Icons.Outlined.Close, contentDescription = null, tint = if (covered) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp)) }; Spacer(Modifier.width(10.dp)); Text(label, fontWeight = FontWeight.SemiBold); Spacer(Modifier.weight(1f)); Text(if (covered) "Covered" else "Missing", color = if (covered) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) }
}

@Composable
private fun ConnectionCard(connection: ConnectionProfile, status: String, viewModel: HackLinkViewModel) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(connection.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""), connection.avatarColor, size = 48)
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) { Text(connection.name, fontWeight = FontWeight.Bold); Text(connection.role, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp); Text(connection.skills.joinToString(" · "), color = MaterialTheme.colorScheme.primary, fontSize = 11.sp) }
            when (status) {
                "Connect" -> OutlinedButton(onClick = { viewModel.sendConnection(connection.id) }, contentPadding = PaddingValues(horizontal = 10.dp), modifier = Modifier.height(40.dp), shape = RoundedCornerShape(11.dp)) { Text("Connect", fontSize = 12.sp) }
                "Accept" -> PrimaryButton("Accept", onClick = { viewModel.acceptConnection(connection.id) }, modifier = Modifier.height(40.dp), icon = Icons.Outlined.Check)
                else -> Text(status, color = if (status == "Connected") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold); Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) } }
    }
}

@Composable
private fun ChoiceSection(title: String, content: @Composable () -> Unit) {
    Column { Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp); Spacer(Modifier.height(7.dp)); content() }
}

@Composable
private fun SetupField(label: String, value: String, minLines: Int = 1, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), label = { Text(label) }, minLines = minLines, singleLine = minLines == 1, shape = RoundedCornerShape(14.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleTopBar(title: String, navController: NavController, showBack: Boolean = true) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun setupChipColors() = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer, selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer)

private fun List<String>.toggle(value: String): List<String> = if (value in this) this - value else this + value

@Composable
fun NotificationsScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    Scaffold(topBar = { SimpleTopBar("Notifications", navController) }) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                    Column(Modifier.weight(1f)) {
                        Text("Stay in the loop", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(5.dp))
                        Text("Deadlines, teammates and opportunities in one place.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    }
                    TextButton(onClick = viewModel::markAllNotificationsRead) { Text("Read all", fontSize = 12.sp) }
                }
                Spacer(Modifier.height(8.dp))
            }
            items(notifications, key = { it.id }) { notification ->
                val tint = when (notification.type) { NotificationType.DEADLINE -> Color(0xFFD97706); NotificationType.TEAM -> MaterialTheme.colorScheme.primary; NotificationType.OPPORTUNITY -> MaterialTheme.colorScheme.tertiary }
                Card(onClick = {
                    viewModel.markNotificationRead(notification.id)
                    when (notification.type) { NotificationType.DEADLINE -> navController.navigate("hackathonDetail"); NotificationType.TEAM -> navController.navigate("teams"); NotificationType.OPPORTUNITY -> navController.navigate("internships") }
                }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (notification.isUnread) tint.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                    Row(Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
                        Box(Modifier.padding(top = 4.dp).size(10.dp).clip(CircleShape).background(if (notification.isUnread) tint else Color.Transparent))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) { Text(notification.title, fontWeight = FontWeight.Bold, fontSize = 14.sp); Spacer(Modifier.height(3.dp)); Text(notification.body, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 18.sp); Spacer(Modifier.height(7.dp)); Text(notification.timestamp, color = tint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
        }
    }
}
