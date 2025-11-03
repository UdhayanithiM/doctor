package com.example.sparkapp.ui.screens.doctor

import androidx.compose.foundation.background // <-- ADDED IMPORT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape // <-- ADDED IMPORT
import androidx.compose.foundation.shape.RoundedCornerShape // <-- ADDED IMPORT
import androidx.compose.material.icons.Icons
// --- UPDATED ICON IMPORTS ---
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
// ---
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // <-- ADDED IMPORT
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparkapp.network.ScoreboardResponse // <-- ADDED IMPORT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScoreboardScreen(
    viewModel: DoctorViewModel, // Pass the *same* ViewModel
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (ScoreboardResponse) -> Unit
) {
    val uiState = viewModel.scoreboardUiState

    // Call fetchScores when this screen is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchScoreboard()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Counselor Response Scoreboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null) // <-- UPDATED ICON
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
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.errorMessage, color = Color.Red, fontSize = 16.sp)
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(uiState.scores) { score ->
                        ScoreboardCard(score = score, onClick = { onNavigateToDetail(score) })
                    }
                }
            }
        }
    }
}

// This is the <Card> item from the Flutter ScoreboardPage
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardCard(score: ScoreboardResponse, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(12.dp) // <-- IMPORT ADDED
    ) {
        ListItem(
            leadingContent = {
                // --- REPLACED 'CircleAvatar' with correct M3 implementation ---
                Box(
                    modifier = Modifier
                        .size(40.dp) // Standard ListItem icon size
                        .clip(CircleShape) // <-- IMPORT ADDED
                        .background(MaterialTheme.colorScheme.primary), // <-- IMPORT ADDED
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        score.score ?: "0",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                // --- END OF FIX ---
            },
            headlineContent = { Text(score.username ?: "Unknown User") }, // <-- UPDATED PARAMETER
            supportingContent = { Text("Scenario: ${score.scenario ?: "N/A"}") }, // <-- UPDATED PARAMETER
            trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(18.dp)) } // <-- UPDATED ICON
        )
    }
}