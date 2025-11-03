package com.example.sparkapp.ui.screens.doctor

// --- IMPORT STATEMENTS ---
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
// Corrected Imports for icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // 'getValue' is not needed here
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sparkapp.network.MessageResponse // <-- 1. IMPORT THE CORRECT DATA CLASS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorNotificationScreen(
    viewModel: DoctorViewModel, // Pass the *same* ViewModel
    onNavigateBack: () -> Unit
) {
    // 2. This is the correct way to access the state from the ViewModel
    val messages = viewModel.notificationState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        // 3. Use the corrected AutoMirrored icon
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No notifications yet.")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(paddingValues)
            ) {
                items(messages) { msg ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            leadingContent = {
                                Icon(
                                    // 4. Use the corrected AutoMirrored icon
                                    Icons.AutoMirrored.Filled.Message,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            // 5. Use 'headlineContent' and 'supportingContent' (M3 API)
                            // These fields now work because 'msg' is a 'MessageResponse'
                            headlineContent = { Text(msg.message ?: "No content") },
                            supportingContent = { Text("From: ${msg.senderId}") }
                        )
                    }
                }
            }
        }
    }
}