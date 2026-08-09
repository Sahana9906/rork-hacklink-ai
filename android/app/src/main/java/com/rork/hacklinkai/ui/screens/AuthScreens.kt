package com.rork.hacklinkai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rork.hacklinkai.ui.components.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1300)
        navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } }
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF171337), Color(0xFF5B4CF6)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(92.dp).clip(RoundedCornerShape(28.dp)).background(Color.White.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.height(22.dp))
            Text("HackLink AI", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(5.dp))
            Text("Build what’s next, together.", color = Color.White.copy(alpha = 0.72f), fontSize = 14.sp)
        }
    }
}

@Composable
fun OnboardingScreen(navController: NavController) {
    var page by remember { mutableIntStateOf(0) }
    val titles = listOf("Discover the right hackathons", "Build the right team", "Turn projects into opportunities")
    val bodies = listOf(
        "Find events based on your actual skills, interests, and the projects you want to build.",
        "AI finds teammates with complementary skills so every role on your team is covered.",
        "Discover internships that understand your skills, project experience, and momentum."
    )
    val icons = listOf(Icons.Outlined.Code, Icons.Outlined.Groups, Icons.Outlined.WorkOutline)
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { navController.navigate("login") { popUpTo("onboarding") { inclusive = true } } }) { Text("Skip") }
        }
        Spacer(Modifier.height(34.dp))
        Box(
            modifier = Modifier.size(210.dp).clip(RoundedCornerShape(58.dp)).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.size(130.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Icon(icons[page], contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
            }
        }
        Spacer(Modifier.height(48.dp))
        Text(titles[page], style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(14.dp))
        Text(bodies[page], color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, fontSize = 15.sp, lineHeight = 23.sp)
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            repeat(3) { index ->
                Box(Modifier.size(if (index == page) 26.dp else 7.dp, 7.dp).clip(CircleShape).background(if (index == page) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline))
            }
        }
        Spacer(Modifier.height(28.dp))
        PrimaryButton(
            text = if (page == 2) "Get Started" else "Continue",
            onClick = {
                if (page == 2) navController.navigate("login") { popUpTo("onboarding") { inclusive = true } } else page += 1
            },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Outlined.ArrowForward
        )
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(64.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(22.dp))
        Text("Welcome back", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(7.dp))
        Text("Your next great build starts here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(38.dp))
        AuthButton("Continue with Google", Icons.Outlined.AutoAwesome) { openHome(navController) }
        Spacer(Modifier.height(12.dp))
        AuthButton("Continue with GitHub", Icons.Outlined.Code) { openHome(navController) }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = { openHome(navController) }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(15.dp)) {
            Text("Continue with Email", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(28.dp))
        Text("By continuing, you agree to HackLink's prototype terms.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("New to HackLink?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = { openHome(navController) }) { Text("Create account", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun AuthButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(15.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
        elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(19.dp))
        Spacer(Modifier.size(10.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

private fun openHome(navController: NavController) {
    navController.navigate("home") { popUpTo("login") { inclusive = true } }
}
