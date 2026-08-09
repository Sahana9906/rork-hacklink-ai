package com.rork.hacklinkai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rork.hacklinkai.model.Hackathon
import com.rork.hacklinkai.model.Internship
import com.rork.hacklinkai.model.TeammateMatch

@Composable
fun MatchBadge(
    match: Int,
    modifier: Modifier = Modifier
) {
    val color = if (match >= 90) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = "$match% match",
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SkillChip(
    label: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        leadingIcon = if (selected) {
            { Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
        } else null,
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            labelColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)
        )
    )
}

@Composable
fun Avatar(
    initials: String,
    color: Long = 0xFF5B4CF6,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color(color)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = (size / 3.2f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    action: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (action != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(action, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ProfileStrengthCard(
    strength: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Your developer profile", color = Color.White.copy(alpha = 0.78f), fontSize = 13.sp)
                    Text("Profile strength", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$strength%", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text("strong", color = Color.White.copy(alpha = 0.72f), fontSize = 11.sp)
                }
            }
            Spacer(Modifier.height(18.dp))
            LinearProgressIndicator(
                progress = { strength / 100f },
                modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(8.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("AI profile is ready to improve your matches", color = Color.White.copy(alpha = 0.82f), fontSize = 12.sp, modifier = Modifier.weight(1f))
                Icon(Icons.Outlined.ArrowForward, contentDescription = "View profile", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun HackathonCard(
    hackathon: Hackathon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(if (compact) 16.dp else 18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(if (compact) 38.dp else 44.dp).clip(RoundedCornerShape(13.dp)).background(Color(hackathon.accentHex).copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.RocketLaunch, contentDescription = null, tint = Color(hackathon.accentHex), modifier = Modifier.size(21.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(hackathon.name, fontWeight = FontWeight.Bold, fontSize = if (compact) 15.sp else 17.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(4.dp))
                    Text(hackathon.category, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
                MatchBadge(hackathon.match)
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                InfoMini(Icons.Outlined.Language, hackathon.mode)
                InfoMini(Icons.Outlined.AccessTime, hackathon.duration)
                InfoMini(Icons.Outlined.CalendarMonth, hackathon.date.substringBefore(" "))
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                hackathon.requiredSkills.take(if (compact) 3 else 4).forEach { SkillChip(it) }
            }
        }
    }
}

@Composable
private fun InfoMini(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun TeammateCard(
    teammate: TeammateMatch,
    isPending: Boolean,
    onConnect: () -> Unit,
    onWhyMatch: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Avatar(teammate.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""), teammate.avatarColor, size = 48)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(teammate.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(teammate.role, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        teammate.skills.forEach { SkillChip(it) }
                    }
                }
                MatchBadge(teammate.compatibility)
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Groups, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(5.dp))
                Text("Complements your backend + AI skill set", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = onWhyMatch, contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp)) {
                    Text("Why?", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlineButton("View profile", onView, Modifier.weight(1f))
                PrimaryButton(if (isPending) "Pending" else "Connect", onConnect, Modifier.weight(1f), icon = if (isPending) Icons.Outlined.Check else Icons.Outlined.PersonAdd, enabled = !isPending)
            }
        }
    }
}

@Composable
fun InternshipCard(
    internship: Internship,
    isSaved: Boolean,
    onClick: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(if (compact) 16.dp else 18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(13.dp)).background(Color(internship.accentHex).copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.WorkOutline, contentDescription = null, tint = Color(internship.accentHex), modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(internship.company, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    Text(internship.role, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Column(horizontalAlignment = Alignment.End) {
                    MatchBadge(internship.match)
                    IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
                        Icon(if (isSaved) Icons.Outlined.Check else Icons.Outlined.CalendarMonth, contentDescription = if (isSaved) "Saved" else "Save opportunity", tint = if (isSaved) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoMini(Icons.Outlined.LocationOn, internship.location)
                InfoMini(Icons.Outlined.Language, internship.mode)
                InfoMini(Icons.Outlined.AccessTime, internship.duration)
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                internship.requiredSkills.take(if (compact) 3 else 4).forEach { SkillChip(it, selected = it in listOf("Java", "Git", "REST APIs")) }
            }
            Spacer(Modifier.height(10.dp))
            Text("Skill gap: ${internship.skillGap}", color = MaterialTheme.colorScheme.tertiary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun DeadlineCard(
    title: String,
    subtitle: String,
    days: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF6E7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF59E0B).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = Color(0xFFD97706))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF7C4A03))
                Text(subtitle, color = Color(0xFF9A6B22), fontSize = 12.sp)
            }
            Text(days, color = Color(0xFFB45309), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("home", "Home", Icons.Outlined.RocketLaunch),
        NavItem("hackathons", "Hackathons", Icons.Outlined.Code),
        NavItem("teams", "Teams", Icons.Outlined.Groups),
        NavItem("internships", "Internships", Icons.Outlined.WorkOutline),
        NavItem("profile", "Profile", Icons.Outlined.PersonAdd)
    )
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route || (item.route == "teams" && currentRoute in setOf("teamBuilder", "teammateProfile", "teamDetails", "invitation"))
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun EmptyState(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(5.dp))
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.material3.CircularProgressIndicator()
        Spacer(Modifier.height(12.dp))
        Text("Preparing your matches…", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
