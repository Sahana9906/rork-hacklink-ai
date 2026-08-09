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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rork.hacklinkai.ui.components.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(900)
        if (navController.currentDestination?.route == "splash") {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp).imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(1.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(78.dp).clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = "HackLink AI", tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Spacer(Modifier.height(18.dp))
            Text("HackLink AI", color = MaterialTheme.colorScheme.onBackground, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text("Find. Match. Build. Win.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextButton(onClick = {
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
            }) { Text("Start onboarding") }
            OutlinedButton(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Open demo workspace", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun OnboardingScreen(navController: NavController) {
    var page by remember { mutableIntStateOf(0) }
    val titles = listOf("Find the right hackathon", "Build the right team", "Turn skills into opportunities")
    val bodies = listOf(
        "Discover hackathons that match your skills, interests and experience.",
        "Find teammates who complement your skills and fill the gaps in your team.",
        "Discover internships based on your projects, experience and skills."
    )
    val icons = listOf(Icons.Outlined.Code, Icons.Outlined.Groups, Icons.Outlined.WorkOutline)
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { openLogin(navController) }) { Text("Skip") }
        }
        Spacer(Modifier.height(28.dp))
        Box(
            modifier = Modifier.size(210.dp).clip(RoundedCornerShape(54.dp)).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.size(124.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Icon(icons[page], contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(62.dp))
            }
        }
        Spacer(Modifier.height(42.dp))
        Text(titles[page], style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(14.dp))
        Text(bodies[page], color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, fontSize = 15.sp, lineHeight = 23.sp)
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            repeat(3) { index ->
                Box(
                    Modifier.size(if (index == page) 26.dp else 7.dp, 7.dp).clip(CircleShape)
                        .background(if (index == page) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                )
            }
        }
        Spacer(Modifier.height(26.dp))
        PrimaryButton(
            text = if (page == 2) "Get Started" else "Next",
            onClick = { if (page == 2) openLogin(navController) else page += 1 },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Outlined.ArrowForward
        )
        TextButton(onClick = { openHome(navController) }) {
            Text("Explore demo workspace", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandMark()
        Spacer(Modifier.height(20.dp))
        Text("Welcome back", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(6.dp))
        Text("Your next great build starts here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(30.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Outlined.MailOutline, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {}) { Text("Forgot password?") }
        }
        PrimaryButton("Login", onClick = { openHome(navController) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(18.dp))
        Text("or continue with", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Spacer(Modifier.height(12.dp))
        AuthButton("Continue with GitHub", Icons.Outlined.Code) { openHome(navController) }
        Spacer(Modifier.height(10.dp))
        AuthButton("Continue with LinkedIn", Icons.Outlined.WorkOutline) { openHome(navController) }
        TextButton(onClick = { openHome(navController) }) {
            Text("Explore demo workspace", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("New to HackLink?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = { navController.navigate("signup") }) { Text("Create account", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(horizontal = 24.dp, vertical = 28.dp)) {
        TextButton(onClick = { navController.popBackStack() }) { Text("Back to login") }
        Spacer(Modifier.height(12.dp))
        Text("Create your profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(7.dp))
        Text("Bring your skills, projects and next big idea into one place.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        Spacer(Modifier.height(26.dp))
        FormField("Full name", name, { name = it })
        Spacer(Modifier.height(12.dp))
        FormField("Email", email, { email = it })
        Spacer(Modifier.height(12.dp))
        PasswordField("Password", password, { password = it })
        Spacer(Modifier.height(12.dp))
        PasswordField("Confirm password", confirmPassword, { confirmPassword = it })
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Create account", onClick = { navController.navigate("profileSetup") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Text("You can edit all profile details later.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun BrandMark() {
    Box(Modifier.size(64.dp).clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
        Icon(Icons.Outlined.AutoAwesome, contentDescription = "HackLink AI", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun AuthButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(19.dp))
        Spacer(Modifier.size(10.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), label = { Text(label) }, singleLine = true, shape = RoundedCornerShape(14.dp))
}

@Composable
private fun PasswordField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(), label = { Text(label) }, visualTransformation = PasswordVisualTransformation(), singleLine = true, shape = RoundedCornerShape(14.dp))
}

private fun openLogin(navController: NavController) {
    if (navController.currentDestination?.route == "login") return
    navController.navigate("login") {
        popUpTo("onboarding") { inclusive = true }
        launchSingleTop = true
    }
}

private fun openHome(navController: NavController) {
    if (navController.currentDestination?.route == "home") return
    navController.navigate("home") {
        popUpTo("login") { inclusive = true }
        launchSingleTop = true
    }
}
