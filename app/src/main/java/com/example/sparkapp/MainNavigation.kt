package com.example.sparkapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType // <-- ADDED IMPORT
import androidx.navigation.navArgument // <-- ADDED IMPORT
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sparkapp.ui.screens.SplashScreen
import com.example.sparkapp.ui.screens.counselor.CounselorDashboardScreen
import com.example.sparkapp.ui.screens.login.LoginScreen
import com.example.sparkapp.ui.screens.module.FullScreenPlayerScreen // <-- ADDED IMPORT
import com.example.sparkapp.ui.screens.module.ModuleScreen // <-- ADDED IMPORT
import com.example.sparkapp.ui.screens.pretest.PreTestScreen
import com.example.sparkapp.ui.screens.scenario.ScenarioScreen // <-- ADDED IMPORT
import com.example.sparkapp.ui.screens.signup.SignUpScreen

// Define all our app's "routes"
object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val COUNSELOR_HOME = "counselor_home"
    const val DOCTOR_HOME = "doctor_home"
    const val PARENT_HOME = "parent_home"
    const val PRE_TEST = "pre_test"
    const val REFERRAL = "referral"

    // --- ADDED NEW ROUTE ---
    const val MODULE_PAGE = "module_page"
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {

        composable(AppRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(AppRoutes.SIGNUP) {
            SignUpScreen(navController = navController)
        }
        composable(AppRoutes.COUNSELOR_HOME) {
            CounselorDashboardScreen(mainNavController = navController)
        }
        composable(AppRoutes.DOCTOR_HOME) {
            Text("DOCTOR HOME PLACEHOLDER")
        }
        composable(AppRoutes.PARENT_HOME) {
            Text("PARENT HOME PLACEHOLDER")
        }

        // --- THIS IS THE UPDATED ROUTE ---
        composable(AppRoutes.PRE_TEST) {
            PreTestScreen(navController = navController)
        }

        composable(AppRoutes.REFERRAL) {
            Text("REFERRAL SCREEN PLACEHOLDER")
        }

        // --- REPLACED PLACEHOLDER WITH NEW ROUTES ---

        // Route for the new 17-video list
        composable(route = "module") {
            ModuleScreen(navController = navController)
        }

        // Route for the fullscreen player
        // This route takes the video ID as an argument
        composable(
            route = "fullscreen_player/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getInt("videoId")
            if (videoId != null) {
                FullScreenPlayerScreen(navController = navController, videoId = videoId)
            } else {
                navController.popBackStack() // Go back if ID is missing
            }
        }

        // --- NEWLY ADDED ROUTES ---

        // Route for the Case Scenario quiz
        composable(route = "scenario") {
            ScenarioScreen(navController = navController)
        }

        // This is the NEXT screen we will build
        composable(route = "checklist") {
            // ChecklistScreen(navController = navController)
            // For now, just add a placeholder
            Text("Checklist Page Placeholder")
        }
    }
}