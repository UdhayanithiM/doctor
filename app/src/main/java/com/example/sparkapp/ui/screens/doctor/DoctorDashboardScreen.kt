package com.example.sparkapp.ui.screens.doctor

import androidx.compose.foundation.background // <-- IMPORT ADDED
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState // <-- IMPORT ADDED
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // <-- IMPORT ADDED
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparkapp.network.ReferralResponse
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDashboardScreen(
    viewModel: DoctorViewModel = viewModel(),
    onNavigateToScoreboard: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState = viewModel.uiState
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    val snackbarHostState = remember { SnackbarHostState() }

    // This collects the new message event from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.newNotificationEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = "New message from ${message.senderId}: ${message.message?.take(50)}...",
                duration = SnackbarDuration.Short,
                actionLabel = "View"
            ).also {
                if (it == SnackbarResult.ActionPerformed) {
                    onNavigateToNotifications()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Referred Students") },
                actions = {
                    IconButton(
                        onClick = onNavigateToScoreboard
                    ) {
                        Icon(Icons.Default.Scoreboard, "View Scoreboard")
                    }
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(Icons.Default.Notifications, "View Notifications")
                    }
                },
                navigationIcon = {
                    // We add a Logout button here as it's a top-level dashboard
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = Color.White) // Use Color.White to match
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White, // <-- THIS IS THE FIX
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        // This is the main body
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.fetchReferrals() },
            modifier = Modifier.padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.referrals.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.hasError -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Error fetching referrals.",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                }
                uiState.referrals.isEmpty() -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // Make it scrollable for SwipeRefresh
                        contentAlignment = Alignment.Center) {
                        Text("No referrals found.", fontSize = 16.sp)
                    }
                }
                // We have data, show the list
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.referrals) { student ->
                            ReferralCard(student = student)
                        }
                    }
                }
            }
        }
    }
}

// This is the <Card> item from the Flutter ListView.builder
@Composable
fun ReferralCard(student: ReferralResponse) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            modifier = Modifier.padding(8.dp), // Extra padding to match ListTile
            leadingContent = {
                // This is the correct M3 replacement for CircleAvatar
                Box(
                    modifier = Modifier
                        .size(56.dp) // 2 * radius (28)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary), // 'background' is now imported
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.name?.firstOrNull()?.uppercase() ?: "?",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            },
            headlineContent = { // Use headlineContent
                Text(
                    student.name ?: "Unknown",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = { // Use supportingContent
                Column(
                    horizontalAlignment = Alignment.Start // <-- THIS IS THE FIX
                ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Age: ${student.age ?: "N/A"}")
                    Text("Standard: ${student.standard ?: "N/A"}")
                    // These fields were in Flutter UI but not in API.
                    // They will show "N/A" if null.
                    Text("Performance: ${student.performance ?: "N/A"}")
                    Text("Behavior: ${student.behavior ?: "N/A"}")
                }
            }
        )
    }
}