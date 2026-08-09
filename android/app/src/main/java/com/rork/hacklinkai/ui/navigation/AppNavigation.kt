package com.rork.hacklinkai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rork.hacklinkai.ui.HackLinkViewModel
import com.rork.hacklinkai.ui.screens.AiProfileAnalysisScreen
import com.rork.hacklinkai.ui.screens.ConnectSourcesScreen
import com.rork.hacklinkai.ui.screens.ConnectionsScreen
import com.rork.hacklinkai.ui.screens.HackathonDetailScreen
import com.rork.hacklinkai.ui.screens.HackathonsScreen
import com.rork.hacklinkai.ui.screens.HomeScreen
import com.rork.hacklinkai.ui.screens.InternshipDetailScreen
import com.rork.hacklinkai.ui.screens.InternshipsScreen
import com.rork.hacklinkai.ui.screens.InvitationScreen
import com.rork.hacklinkai.ui.screens.LoginScreen
import com.rork.hacklinkai.ui.screens.MyQrScreen
import com.rork.hacklinkai.ui.screens.NotificationsScreen
import com.rork.hacklinkai.ui.screens.OnboardingScreen
import com.rork.hacklinkai.ui.screens.ProfileScreen
import com.rork.hacklinkai.ui.screens.ProfileSetupScreen
import com.rork.hacklinkai.ui.screens.PublicProfileScreen
import com.rork.hacklinkai.ui.screens.QrScannerScreen
import com.rork.hacklinkai.ui.screens.ReviewImportedDataScreen
import com.rork.hacklinkai.ui.screens.SettingsScreen
import com.rork.hacklinkai.ui.screens.SignUpScreen
import com.rork.hacklinkai.ui.screens.SkillEvidenceScreen
import com.rork.hacklinkai.ui.screens.SplashScreen
import com.rork.hacklinkai.ui.screens.TeamBuilderScreen
import com.rork.hacklinkai.ui.screens.TeamDetailsScreen
import com.rork.hacklinkai.ui.screens.TeammateProfileScreen
import com.rork.hacklinkai.ui.screens.TeamsScreen
import com.rork.hacklinkai.ui.screens.TrackerScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: HackLinkViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("profileSetup") { ProfileSetupScreen(navController, viewModel) }
        composable("connectSources") { ConnectSourcesScreen(navController, viewModel) }
        composable("reviewImported") { ReviewImportedDataScreen(navController) }
        composable("profileAnalysis") { AiProfileAnalysisScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel) }
        composable("hackathons") { HackathonsScreen(navController) }
        composable("hackathonDetail") { HackathonDetailScreen(navController, viewModel) }
        composable("teamBuilder") { TeamBuilderScreen(navController, viewModel) }
        composable("teammateProfile") { TeammateProfileScreen(navController, viewModel) }
        composable("teams") { TeamsScreen(navController) }
        composable("teamDetails") { TeamDetailsScreen(navController, viewModel) }
        composable("invitation") { InvitationScreen(navController, viewModel) }
        composable("connections") { ConnectionsScreen(navController, viewModel) }
        composable("tracker") { TrackerScreen(navController) }
        composable("internships") { InternshipsScreen(navController, viewModel) }
        composable("internshipDetail") { InternshipDetailScreen(navController, viewModel) }
        composable("profile") { ProfileScreen(navController, viewModel) }
        composable("skillEvidence") { SkillEvidenceScreen(navController) }
        composable("settings") { SettingsScreen(navController, viewModel) }
        composable("myQr") { MyQrScreen(navController) }
        composable("qrScanner") { QrScannerScreen(navController) }
        composable("publicProfile") { PublicProfileScreen(navController, viewModel) }
        composable("notifications") { NotificationsScreen(navController, viewModel) }
    }
}
