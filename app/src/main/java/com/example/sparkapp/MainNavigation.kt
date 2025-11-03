package com.example.sparkapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sparkapp.ui.screens.SplashScreen
import com.example.sparkapp.ui.screens.checklist.ChecklistScreen
import com.example.sparkapp.ui.screens.counselor.CounselorDashboardScreen
import com.example.sparkapp.ui.screens.history.ScoreDetailScreen
import com.example.sparkapp.ui.screens.login.LoginScreen
import com.example.sparkapp.ui.screens.module.FullScreenPlayerScreen
import com.example.sparkapp.ui.screens.module.ModuleScreen
import com.example.sparkapp.ui.screens.posttest.PostTestScreen
import com.example.sparkapp.ui.screens.pretest.PreTestScreen
import com.example.sparkapp.ui.screens.referral.ReferralScreen
import com.example.sparkapp.ui.screens.scenario.ScenarioScreen
import com.example.sparkapp.ui.screens.signup.SignUpScreen
import com.example.sparkapp.network.ScoreboardResponse
import com.example.sparkapp.ui.screens.doctor.DoctorDashboardScreen
import com.example.sparkapp.ui.screens.doctor.DoctorNotificationScreen
import com.example.sparkapp.ui.screens.doctor.DoctorScoreDetailScreen
import com.example.sparkapp.ui.screens.doctor.DoctorScoreboardScreen
import com.example.sparkapp.ui.screens.doctor.DoctorViewModel
import com.google.gson.Gson
import com.example.sparkapp.ui.screens.parent.ParentDashboardScreen
import com.example.sparkapp.ui.screens.parent.ParentProfileScreen
import com.example.sparkapp.ui.screens.parent.ParentViewModel

// Define all our app's "routes"
object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    // Note: The new Login logic in your prompt uses "counselorDashboard", not "counselor_home"
    const val COUNSELOR_HOME = "counselorDashboard"
    const val DOCTOR_HOME = "doctorDashboard"
    const val PARENT_HOME = "parentDashboard"
    const val PRE_TEST = "pre_test"
    const val REFERRAL = "referral"
    const val MODULE_PAGE = "module_page"
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    val doctorViewModel: DoctorViewModel = viewModel()
    val parentViewModel: ParentViewModel = viewModel()

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {

        composable(AppRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }

        // --- THIS BLOCK IS CORRECTED ---
        // These errors mean your LoginScreen.kt file has not been updated
        // to accept 'onLoginSuccess'. We must pass the 'navController'
        // as its definition currently expects.
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }

        // --- THIS BLOCK IS CORRECTED ---
        // This call was missing the 'navController' parameter
        composable(AppRoutes.SIGNUP) {
            SignUpScreen(navController = navController)
        }

        // --- UPDATED COUNSELOR ROUTE (to accept userId) ---
        // This route name must match the one used in your LoginScreen's logic
        composable(
            route = "${AppRoutes.COUNSELOR_HOME}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            CounselorDashboardScreen(
                mainNavController = navController,
                onLogout = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // --- UPDATED PARENT ROUTE ---
        composable(AppRoutes.PARENT_HOME) {
            ParentDashboardScreen(
                onNavigateToProfile = {
                    navController.navigate("parentProfile")
                },
                onLogout = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // --- THIS IS THE UPDATED ROUTE ---
        composable(AppRoutes.PRE_TEST) {
            PreTestScreen(navController = navController)
        }

        composable("create_referral") {
            ReferralScreen(navController = navController)
        }

        composable(route = "module") {
            ModuleScreen(navController = navController)
        }

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

        composable(route = "scenario") {
            ScenarioScreen(navController = navController)
        }

        // --- THIS IS THE FIXED BLOCK ---
        composable(route = "checklist") {
            ChecklistScreen(
                onProceed = {
                    // Navigate to the post-test (route name is "post_test")
                    navController.navigate("post_test")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        // --- END OF FIX ---

        composable("post_test") {
            PostTestScreen(navController = navController)
        }

        composable(
            route = "score_detail/{name}/{score}/{total}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("score") { type = NavType.StringType },
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ScoreDetailScreen(
                navController = navController,
                name = backStackEntry.arguments?.getString("name") ?: "Unknown",
                score = backStackEntry.arguments?.getString("score") ?: "0",
                total = backStackEntry.arguments?.getString("total") ?: "0"
            )
        }

        // --- ADDED THE NEW DOCTOR FLOW ROUTES ---

        composable(AppRoutes.DOCTOR_HOME) {
            DoctorDashboardScreen(
                viewModel = doctorViewModel,
                onLogout = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToScoreboard = {
                    navController.navigate("doctorScoreboard")
                },
                onNavigateToNotifications = {
                    navController.navigate("doctorNotifications")
                }
            )
        }

        composable("doctorNotifications") {
            DoctorNotificationScreen(
                viewModel = doctorViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("doctorScoreboard") {
            DoctorScoreboardScreen(
                viewModel = doctorViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { scoreItem ->
                    val scoreJson = Gson().toJson(scoreItem)
                    navController.navigate("doctorScoreDetail/$scoreJson")
                }
            )
        }

        composable("doctorScoreDetail/{scoreJson}") { backStackEntry ->
            val scoreJson = backStackEntry.arguments?.getString("scoreJson")
            val scoreItem = Gson().fromJson(scoreJson, ScoreboardResponse::class.java)
            DoctorScoreDetailScreen(
                score = scoreItem,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- ADD THE NEW PARENT FLOW ROUTES ---

        composable("parentProfile") {
            ParentProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = parentViewModel
            )
        }
    }
}