package com.rork.hacklinkai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.screens.HackathonDetailScreen
import com.rork.hacklinkai.ui.screens.HackathonsScreen
import com.rork.hacklinkai.ui.screens.HomeScreen
import com.rork.hacklinkai.ui.screens.InternshipDetailScreen
import com.rork.hacklinkai.ui.screens.InternshipsScreen
import com.rork.hacklinkai.ui.screens.LoginScreen
import com.rork.hacklinkai.ui.screens.MyQrScreen
import com.rork.hacklinkai.ui.screens.NotificationsScreen
import com.rork.hacklinkai.ui.screens.OnboardingScreen
import com.rork.hacklinkai.ui.screens.ProfileScreen
import com.rork.hacklinkai.ui.screens.QrScannerScreen
import com.rork.hacklinkai.ui.screens.SplashScreen
import com.rork.hacklinkai.ui.screens.TeamBuilderScreen
import com.rork.hacklinkai.ui.screens.TeammateProfileScreen
import com.rork.hacklinkai.ui.screens.TrackerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: HackLinkViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel) }
        composable("hackathons") { HackathonsScreen(navController) }
        composable("hackathonDetail") { HackathonDetailScreen(navController, viewModel) }
        composable("teamBuilder") { TeamBuilderScreen(navController, viewModel) }
        composable("teammateProfile") { TeammateProfileScreen(navController, viewModel) }
        composable("tracker") { TrackerScreen(navController) }
        composable("internships") { InternshipsScreen(navController, viewModel) }
        composable("internshipDetail") { InternshipDetailScreen(navController, viewModel) }
        composable("profile") { ProfileScreen(navController, viewModel) }
        composable("myQr") { MyQrScreen(navController) }
        composable("qrScanner") { QrScannerScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }
    }
}
