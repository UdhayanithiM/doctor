package com.example.sparkapp.ui.screens.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sparkapp.data.ChecklistData
import com.example.sparkapp.ui.theme.ButtonColor // Assuming 0xFF6A5AE0 is defined in Color.kt
import com.example.sparkapp.ui.theme.ChecklistHeaderBg // Assuming 0xFFDFE3FF
import com.example.sparkapp.ui.theme.ChecklistItemBg // Assuming 0xFFF2F2F2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(navController: NavController) {
    val sections = ChecklistData.sections

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checklist", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ButtonColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(sections) { section ->
                // Section Title (Blue box)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(ChecklistHeaderBg)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = section.title,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black // Adjust color as needed
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Section Items (Grey box)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ChecklistItemBg)
                        .padding(12.dp)
                ) {
                    for (item in section.items) {
                        Row(
                            crossAxisAlignment = CrossAxisAlignment.start,
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = "● ",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                            )
                            Text(
                                text = item,
                                style = TextStyle(fontSize = 14.sp, color = Color.Black),
                                modifier = Modifier.padding(end = 4.dp) // Ensure text wraps
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // "Proceed to Post-Test" Button
            item {
                Button(
                    onClick = {
                        // Navigate to PostTestPage (knowledge_page.dart)
                        navController.navigate("post_test")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Proceed to Post-Test",
                        style = TextStyle(fontSize = 16.sp, color = Color.White)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp)) // Add padding at the bottom
            }
        }
    }
}