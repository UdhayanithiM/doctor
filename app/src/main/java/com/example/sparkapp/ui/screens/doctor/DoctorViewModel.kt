package com.example.sparkapp.ui.screens.doctor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparkapp.network.ApiService
import com.example.sparkapp.network.MessageResponse
import com.example.sparkapp.network.ReferralResponse
import com.example.sparkapp.network.RetrofitClient
import com.example.sparkapp.network.ScoreboardResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// --- UI State for the Main Referral List ---
data class DoctorUiState(
    val referrals: List<ReferralResponse> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)

// --- UI State for the Scoreboard Page ---
data class ScoreboardUiState(
    val scores: List<ScoreboardResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class DoctorViewModel : ViewModel() {

    var uiState by mutableStateOf(DoctorUiState())
        private set

    var scoreboardUiState by mutableStateOf(ScoreboardUiState())
        private set

    // For notifications
    var notificationState by mutableStateOf<List<MessageResponse>>(emptyList())
        private set

    // To show a snackbar for new messages
    private val _newNotificationEvent = MutableSharedFlow<MessageResponse>()
    val newNotificationEvent = _newNotificationEvent.asSharedFlow()

    private val apiService: ApiService = RetrofitClient.instance

    // Hardcoded IDs from Flutter code
    private val DOCTOR_ID = "1"
    private val STUDENT_ID = "2" // This "receiver_id" seems to be for a specific student?

    init {
        fetchReferrals()
        startMessagePolling()
    }

    fun fetchReferrals() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, hasError = false)
            try {
                val response = apiService.getDoctorReferrals()
                if (response.isSuccessful) {
                    uiState = uiState.copy(
                        referrals = response.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    uiState = uiState.copy(isLoading = false, hasError = true)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, hasError = true)
            }
        }
    }

    private fun startMessagePolling() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    // We poll for messages where the doctor is the receiver
                    val response = apiService.getMessages(senderId = STUDENT_ID, receiverId = DOCTOR_ID)
                    if (response.isSuccessful) {
                        val allMessages = response.body() ?: emptyList()
                        val currentIds = notificationState.map { it.id }.toSet()

                        // Check for new messages
                        allMessages.forEach { newMsg ->
                            if (newMsg.id !in currentIds) {
                                // Add to state
                                notificationState = notificationState + newMsg
                                // Emit event to show Snackbar
                                _newNotificationEvent.emit(newMsg)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Polling error, silent fail
                    e.printStackTrace()
                }
                delay(3000) // 3-second delay, just like Flutter
            }
        }
    }

    fun fetchScoreboard() {
        viewModelScope.launch {
            scoreboardUiState = scoreboardUiState.copy(isLoading = true)
            try {
                val response = apiService.getScoreboard()
                if (response.isSuccessful) {
                    scoreboardUiState = scoreboardUiState.copy(
                        scores = response.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    scoreboardUiState = scoreboardUiState.copy(
                        isLoading = false,
                        errorMessage = "Failed to load scores"
                    )
                }
            } catch (e: Exception) {
                scoreboardUiState = scoreboardUiState.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }
}