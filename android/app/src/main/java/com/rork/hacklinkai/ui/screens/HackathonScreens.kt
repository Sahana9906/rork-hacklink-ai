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
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.components.BottomNavigationBar
import com.rork.hacklinkai.ui.components.HackathonCard
import com.rork.hacklinkai.ui.components.MatchBadge
import com.rork.hacklinkai.ui.components.OutlineButton
import com.rork.hacklinkai.ui.components.PrimaryButton
import com.rork.hacklinkai.ui.components.SectionHeader
import com.rork.hacklinkai.ui.components.SkillChip
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonsScreen(navController: NavController) {
    var search by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "AI", "Web", "Android", "Cloud", "Cybersecurity", "FinTech")
    val filtered = MockData.hackathons.filter { hackathon ->
        val matchesSearch = search.isBlank() || hackathon.name.contains(search, ignoreCase = true) || hackathon.category.contains(search, ignoreCase = true)
        val matchesFilter = selectedFilter == "All" || hackathon.category.contains(selectedFilter, ignoreCase = true) || hackathon.requiredSkills.any { it.contains(selectedFilter, ignoreCase = true) }
        matchesSearch && matchesFilter
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hackathons", fontWeight = FontWeight.ExtraBold) },
                actions = { IconButton(onClick = {}) { Icon(Icons.Outlined.FilterList, contentDescription = "Filter") } }
            )
        },
        bottomBar = { BottomNavigationBar(navController, "hackathons") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text("Curated for your next build", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Matches are based on your skills, interests, and availability.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search hackathons") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp)
                )
            }
            item {
                androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 2.dp)) {
                    items(filters.size) { index ->
                        FilterChip(
                            selected = selectedFilter == filters[index],
                            onClick = { selectedFilter = filters[index] },
                            label = { Text(filters[index]) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer, selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer)
                        )
                    }
                }
            }
            item { Text("${filtered.size} opportunities", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
            items(filtered, key = { it.id }) { hackathon ->
                HackathonCard(hackathon, onClick = { navController.navigate("hackathonDetail") })
            }
            if (filtered.isEmpty()) {
                item { com.rork.hacklinkai.ui.components.EmptyState("No matches yet", "Try a different search or skill filter.") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackathonDetailScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val hackathon = MockData.hackathons.first()
    val joined by viewModel.joinedHackathon.collectAsStateWithLifecycle()
    var showRegister by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hackathon details", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Card(shape = RoundedCornerShape(25.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                    Column(Modifier.padding(22.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Column(Modifier.weight(1f)) {
                                Text("GOOGLE GENAI HACKATHON", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Spacer(Modifier.height(9.dp))
                                Text(hackathon.name, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                            }
                            MatchBadge(hackathon.match)
                        }
                        Spacer(Modifier.height(18.dp))
                        Text("Build the next generation of helpful AI experiences with Gemini and Google Cloud.", color = Color.White.copy(alpha = 0.82f), fontSize = 13.sp, lineHeight = 19.sp)
                    }
                }
            }
            item { SectionHeader("Overview") }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OverviewCell(Icons.Outlined.CalendarMonth, "Date", hackathon.date, Modifier.weight(1f))
                    OverviewCell(Icons.Outlined.Language, "Mode", hackathon.mode, Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OverviewCell(Icons.Outlined.AccessTime, "Duration", hackathon.duration, Modifier.weight(1f))
                    OverviewCell(Icons.Outlined.Groups, "Team size", hackathon.teamSize, Modifier.weight(1f))
                }
            }
            item {
                SectionHeader("Required skills")
                Spacer(Modifier.height(9.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { hackathon.requiredSkills.forEach { SkillChip(it) } }
            }
            item {
                SectionHeader("Tracks")
                Spacer(Modifier.height(9.dp))
                Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    hackathon.tracks.forEach { track ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                            Spacer(Modifier.size(10.dp))
                            Text(track, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(17.dp)) {
                        Text("Why this matches you", color = MaterialTheme.colorScheme.onTertiaryContainer, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(10.dp))
                        hackathon.matchingReasons.forEach { reason ->
                            Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(reason, color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 13.sp)
                            }
                        }
                        HorizontalDivider(Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.18f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Cloud, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Skill gap: ${hackathon.skillGap}", color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlineButton(if (joined) "Joined" else "Register", onClick = { showRegister = true }, Modifier.weight(1f), icon = Icons.Outlined.Check)
                    PrimaryButton("Find team", onClick = { navController.navigate("teamBuilder") }, Modifier.weight(1f), icon = Icons.Outlined.Groups)
                }
            }
            if (joined) {
                item { TextButton(onClick = { navController.navigate("tracker") }, modifier = Modifier.fillMaxWidth()) { Text("View hackathon tracker") } }
            }
        }
    }
    if (showRegister) {
        AlertDialog(
            onDismissRequest = { showRegister = false },
            title = { Text("Registration ready") },
            text = { Text("This prototype would open the hackathon's external registration page. Mark it as joined to start tracking your progress.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.joinHackathon()
                    showRegister = false
                    scope.launch { snackbarHostState.showSnackbar("Google GenAI Hackathon added to your tracker") }
                }) { Text("Add to tracker", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showRegister = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun OverviewCell(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(19.dp))
            Spacer(Modifier.height(8.dp))
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Spacer(Modifier.height(2.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hackathon tracker", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item {
                Text("Google GenAI Hackathon", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Your team is moving. Keep the momentum.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(22.dp)) {
                    Column(Modifier.padding(18.dp)) {
                        TimelineRow("Registration", "Completed", true, "Oct 01", isLast = false)
                        TimelineRow("Team formation", "Completed", true, "Oct 03", isLast = false)
                        TimelineRow("Development", "In progress", true, "Today", isLast = false)
                        TimelineRow("Submission", "2 days remaining", false, "Oct 20", isLast = false, isWarning = true)
                        TimelineRow("Demo", "5 days remaining", false, "Oct 23", isLast = true)
                    }
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF6E7)), shape = RoundedCornerShape(20.dp)) {
                    Row(Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.WarningAmber, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(24.dp))
                        Spacer(Modifier.size(12.dp))
                        Column {
                            Text("Submission is your next milestone", fontWeight = FontWeight.Bold, color = Color(0xFF7C4A03))
                            Text("Review the README and record a 2-minute demo.", color = Color(0xFF9A6B22), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineRow(title: String, status: String, completed: Boolean, date: String, isLast: Boolean, isWarning: Boolean = false) {
    Row(Modifier.fillMaxWidth().height(if (isLast) 68.dp else 84.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(28.dp)) {
            Box(Modifier.size(24.dp).clip(CircleShape).background(if (completed) MaterialTheme.colorScheme.tertiaryContainer else if (isWarning) Color(0xFFFFE7BF) else MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                if (completed) Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(15.dp)) else Text("•", color = if (isWarning) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp)
            }
            if (!isLast) Box(Modifier.width(2.dp).height(50.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)))
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(status, color = if (completed) MaterialTheme.colorScheme.tertiary else if (isWarning) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Text(date, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}
