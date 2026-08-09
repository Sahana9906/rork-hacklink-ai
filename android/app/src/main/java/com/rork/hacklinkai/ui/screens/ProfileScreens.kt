package com.rork.hacklinkai.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rork.hacklinkai.data.MockData
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.components.Avatar
import com.rork.hacklinkai.ui.components.BottomNavigationBar
import com.rork.hacklinkai.ui.components.PrimaryButton
import com.rork.hacklinkai.ui.components.ProfileStrengthCard
import com.rork.hacklinkai.ui.components.SectionHeader
import com.rork.hacklinkai.ui.components.SkillChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: HackLinkViewModel) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile", fontWeight = FontWeight.ExtraBold) }, actions = { IconButton(onClick = { navController.navigate("notifications") }) { Icon(Icons.Outlined.Notifications, contentDescription = "Notifications") } }) },
        bottomBar = { BottomNavigationBar(navController, "profile") }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar("SB", size = 76)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(profile.name, style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                        Text(profile.role, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Spacer(Modifier.height(5.dp))
                        Text("${profile.experienceLevel} • ${profile.availability}", color = androidx.compose.material3.MaterialTheme.colorScheme.primary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    IconButton(onClick = { navController.navigate("profileSetup") }) { Icon(Icons.Outlined.Edit, contentDescription = "Edit profile") }
                }
            }
            item { ProfileStrengthCard(strength = profile.profileStrength, onClick = { navController.navigate("skillEvidence") }) }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard("12", "Projects", Modifier.weight(1f))
                    StatCard("6", "Hackathons", Modifier.weight(1f))
                    StatCard("128", "Connections", Modifier.weight(1f))
                }
            }
            item {
                SectionHeader("Skills")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { profile.skills.take(3).forEach { skill -> SkillChip(skill.name, selected = true, onClick = { navController.navigate("skillEvidence") }) } }
                Spacer(Modifier.height(7.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { profile.skills.drop(3).forEach { skill -> SkillChip(skill.name, selected = true, onClick = { navController.navigate("skillEvidence") }) } }
            }
            item {
                SectionHeader("Achievements")
                Spacer(Modifier.height(8.dp))
                MockData.achievements.forEach { achievement ->
                    Row(Modifier.padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(34.dp).clip(CircleShape).background(Color(0xFFFFF0C7)), contentAlignment = Alignment.Center) { Text("✦", color = Color(0xFFD97706), fontWeight = FontWeight.Bold) }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) { Text(achievement.title, fontWeight = FontWeight.Bold, fontSize = 13.sp); Text(achievement.subtitle, color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp) }
                    }
                }
            }
            item {
                SectionHeader("Profile actions")
                Spacer(Modifier.height(8.dp))
                ActionRow("Edit profile", Icons.Outlined.Edit) { navController.navigate("profileSetup") }
                ActionRow(if (profile.githubConnected) "GitHub connected" else "Connect GitHub", Icons.Outlined.Code) { viewModel.connectGithub() }
                ActionRow(if (profile.linkedinConnected) "LinkedIn connected" else "Connect LinkedIn", Icons.Outlined.Link) { viewModel.connectLinkedin() }
                ActionRow(if (profile.resumeUploaded) "Resume analyzed" else "Upload resume", Icons.Outlined.UploadFile) { viewModel.uploadResume() }
                ActionRow("My QR", Icons.Outlined.QrCode2) { navController.navigate("myQr") }
                ActionRow("Connections", Icons.Outlined.Groups) { navController.navigate("connections") }
                ActionRow("Settings", Icons.Outlined.Settings) { navController.navigate("settings") }
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.padding(vertical = 14.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) { Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold); Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp) }
    }
}

@Composable
private fun ActionRow(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    androidx.compose.material3.TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(vertical = 10.dp, horizontal = 4.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(21.dp))
        Spacer(Modifier.width(13.dp))
        Text(label, modifier = Modifier.weight(1f), textAlign = TextAlign.Start, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyQrScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("My QR", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } }) }) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(20.dp))
            Text("Scan to connect with me", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(6.dp))
            Text("Share your HackLink profile instantly at hackathons.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(26.dp), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
                QrCodeArt(Modifier.padding(22.dp).size(235.dp))
            }
            Spacer(Modifier.height(20.dp))
            Text("Sahana B", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Backend Developer", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            Spacer(Modifier.weight(1f))
            PrimaryButton("Scan a developer QR", onClick = { navController.navigate("qrScanner") }, modifier = Modifier.fillMaxWidth(), icon = Icons.Outlined.CameraAlt)
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun QrCodeArt(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val cell = size.minDimension / 21f
        val dark = Color(0xFF16152A)
        drawRoundRect(Color.White, cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f))
        fun square(x: Int, y: Int, width: Int = 1, height: Int = 1) { drawRect(dark, topLeft = Offset(x * cell, y * cell), size = androidx.compose.ui.geometry.Size(width * cell, height * cell)) }
        fun finder(x: Int, y: Int) { square(x, y, 7, 1); square(x, y + 6, 7, 1); square(x, y, 1, 7); square(x + 6, y, 1, 7); square(x + 2, y + 2, 3, 3) }
        finder(1, 1); finder(13, 1); finder(1, 13)
        val patterns = listOf("1011010110101", "0110101101011", "1101010011010", "0011011100101", "1010110011101", "0101101010110", "1110010110101")
        patterns.forEachIndexed { row, pattern -> pattern.forEachIndexed { column, bit -> if (bit == '1') square(9 + column, 8 + row) } }
        for (y in 9..19) for (x in 9..19) if ((x * 3 + y * 5) % 7 == 0) square(x, y)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(navController: NavController) {
    var scanned by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("Scan developer QR", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Outlined.ArrowBack, contentDescription = "Back") } }) }) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Point your camera at a HackLink QR code", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Box(Modifier.fillMaxWidth().height(360.dp).clip(RoundedCornerShape(28.dp)).background(Color(0xFF171337)), contentAlignment = Alignment.Center) {
                Box(Modifier.size(225.dp).clip(RoundedCornerShape(25.dp)).background(Color.Transparent).then(Modifier.background(Color.White.copy(alpha = 0.03f)))) {
                    Canvas(Modifier.fillMaxSize()) { drawRoundRect(Color(0xFF9B91FF), style = Stroke(width = 4f, pathEffect = PathEffect.cornerPathEffect(18f))) }
                }
                Text("Camera preview", color = Color.White.copy(alpha = 0.55f), fontSize = 12.sp, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp))
            }
            Spacer(Modifier.height(22.dp))
            if (scanned) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
                    Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) { Avatar("SB", 0xFF0EA5A8, size = 48); Spacer(Modifier.width(11.dp)); Column(Modifier.weight(1f)) { Text("Sahana B", fontWeight = FontWeight.Bold); Text("Backend Developer", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp); Text("Java • Spring Boot • AI/ML", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp) }; Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary) }
                }
                Spacer(Modifier.height(13.dp))
                PrimaryButton("Connect", onClick = { navController.navigate("publicProfile") }, modifier = Modifier.fillMaxWidth())
            } else {
                PrimaryButton("Simulate QR scan", onClick = { scanned = true }, modifier = Modifier.fillMaxWidth(), icon = Icons.Outlined.QrCode2)
                Spacer(Modifier.height(9.dp))
                Text("Prototype mode · camera access is not required", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }
        }
    }
}


