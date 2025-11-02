package com.example.sparkapp.ui.screens.counselor

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sparkapp.ui.screens.chat.ChatScreen // <-- ADDED IMPORT
import com.example.sparkapp.ui.screens.history.HistoryScreen
import com.example.sparkapp.ui.theme.SparkAppPurple

// --- This sealed class defines the 4 tabs ---
sealed class CounselorScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : CounselorScreen("counselor_home_content", "Home", Icons.Default.Home)
    object History : CounselorScreen("counselor_history", "History", Icons.Default.AccessTime)
    object Chat : CounselorScreen("counselor_chat", "Chat", Icons.Default.ChatBubbleOutline)
    object Profile : CounselorScreen("counselor_profile", "Profile", Icons.Default.PersonOutline)
}

// This list holds the tab items
val counselorBottomNavItems = listOf(
    CounselorScreen.Home,
    CounselorScreen.History,
    CounselorScreen.Chat,
    CounselorScreen.Profile
)
// ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselorDashboardScreen(mainNavController: NavController) {
    // This creates a *new, nested* NavController just for the 4 tabs
    val tabNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Title is handled by each tab screen */ },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SparkAppPurple,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { mainNavController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Handle notification click */ }) {
                        Icon(Icons.Default.NotificationsNone, "Notifications")
                    }
                }
            )
        },
        bottomBar = {
            // This is your BottomNavigationBar
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                counselorBottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SparkAppPurple,
                            selectedTextColor = SparkAppPurple,
                            unselectedIconColor = Color.Black,
                            unselectedTextColor = Color.Black
                        ),
                        onClick = {
                            tabNavController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(tabNavController.graph.startDestinationId)
                                // Avoid multiple copies of the same destination when re-selecting the same item
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // This is the body that switches between the 4 tab screens
        NavHost(
            navController = tabNavController,
            startDestination = CounselorScreen.Home.route, // Start on the Home tab
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CounselorScreen.Home.route) {
                // CounselorHomeScreen's parameter is 'mainNavController'
                CounselorHomeScreen(mainNavController = mainNavController)
            }
            composable(CounselorScreen.History.route) {
                // HistoryScreen's parameter is 'navController'
                HistoryScreen(navController = mainNavController)
            }
            composable(CounselorScreen.Chat.route) {
                // <-- UPDATED: This is the "Chat" tab's content
                ChatScreen(navController = mainNavController)
            }
            composable(CounselorScreen.Profile.route) {
                // Placeholder for now
                CounselorProfileScreen()
            }
        }
    }
}