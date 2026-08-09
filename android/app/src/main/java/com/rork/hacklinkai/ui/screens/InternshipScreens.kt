package com.rork.hacklinkai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.model.Internship
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.components.BottomNavigationBar
import com.rork.hacklinkai.ui.components.InternshipCard
import com.rork.hacklinkai.ui.components.MatchBadge
import com.rork.hacklinkai.ui.components.PrimaryButton
import com.rork.hacklinkai.ui.components.SectionHeader
import com.rork.hacklinkai.ui.components.SkillChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipsScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val saved by viewModel.savedInternships.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Software Engineering", "AI / ML", "Backend", "Frontend", "Android")
    val filtered = MockData.internships.filter { internship ->
        val searchMatch = search.isBlank() || internship.company.contains(search, true) || internship.role.contains(search, true)
        val filterMatch = filter == "All" || internship.role.contains(filter, true) || (filter == "Backend" && internship.role.contains("Engineering", true))
        searchMatch && filterMatch
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Internships", fontWeight = FontWeight.ExtraBold) }, actions = { IconButton(onClick = {}) { Icon(Icons.Outlined.FilterList, contentDescription = "Filter") } }) },
        bottomBar = { BottomNavigationBar(navController, "internships") }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                Text("Opportunities with context", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(5.dp))
                Text("Your hackathon experience makes your matches sharper.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            item {
                OutlinedTextField(value = search, onValueChange = { search = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Search roles or companies") }, leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) }, singleLine = true, shape = RoundedCornerShape(15.dp))
            }
            item {
                androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 2.dp)) {
                    items(filters.size) { index ->
                        FilterChip(selected = filter == filters[index], onClick = { filter = filters[index] }, label = { Text(filters[index], fontSize = 12.sp) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer, selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer))
                    }
                }
            }
            item { Text("${filtered.size} recommended roles", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
            items(filtered, key = { it.id }) { internship ->
                InternshipCard(internship, internship.id in saved, onClick = { navController.navigate("internshipDetail") }, onSave = { viewModel.toggleSavedInternship(internship.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternshipDetailScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val internship = MockData.internships.first()
    val saved by viewModel.savedInternships.collectAsStateWithLifecycle()
    var applied by remember { mutableStateOf(false) }
    var showApply by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Opportunity", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } }) }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(17.dp)) {
            item {
                Column {
                    Text(internship.company.uppercase(), color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Row(verticalAlignment = androidx.compose.ui.Alignment.Top) {
                        Text(internship.role, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                        MatchBadge(internship.match)
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { SkillChip(internship.location); SkillChip(internship.mode); SkillChip(internship.duration) }
            }
            item { DetailSection("Required skills") { Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { internship.requiredSkills.forEach { SkillChip(it, selected = it in listOf("Java", "Git", "REST APIs")) } } } }
            item { DetailSection("Why you're a match") { listOf("Java experience", "Backend projects", "GitHub activity", "Hackathon experience").forEach { row -> Row(Modifier.padding(vertical = 4.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) { Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary); Spacer(Modifier.size(8.dp)); Text(row, fontSize = 13.sp) } } } }
            item { DetailSection("Recommended improvement") { Text(internship.skillGap, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold, fontSize = 14.sp); Spacer(Modifier.height(5.dp)); Text("A small focused project here could improve your next match.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp) } }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PrimaryButton(if (applied) "Application started" else "Apply", onClick = { showApply = true }, modifier = Modifier.weight(1f), icon = Icons.Outlined.OpenInNew, enabled = !applied)
                    androidx.compose.material3.OutlinedButton(onClick = { viewModel.toggleSavedInternship(internship.id) }, modifier = Modifier.weight(0.45f).height(52.dp), shape = RoundedCornerShape(16.dp)) { Icon(Icons.Outlined.Bookmark, contentDescription = "Save") }
                }
            }
        }
    }
    if (showApply) {
        AlertDialog(onDismissRequest = { showApply = false }, title = { Text("Ready to apply?") }, text = { Text("This prototype would open the company's application page in your browser.") }, confirmButton = { TextButton(onClick = { applied = true; showApply = false }) { Text("Continue") } }, dismissButton = { TextButton(onClick = { showApply = false }) { Text("Not now") } })
    }
}

@Composable
private fun DetailSection(title: String, content: @Composable () -> Unit) {
    Column {
        SectionHeader(title)
        Spacer(Modifier.height(9.dp))
        content()
    }
}
