package com.example.sparkapp.ui.screens.chat

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparkapp.data.Message
import com.example.sparkapp.network.RetrofitClient
import com.example.sparkapp.network.SendMessageRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    // This holds the text field input
    val messageText = mutableStateOf("")

    // This is the list of messages for the UI
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private var senderId: String = "0"

    init {
        // Get the logged-in user's ID
        val prefs = application.getSharedPreferences("SparkAppPrefs", Context.MODE_PRIVATE)
        senderId = prefs.getString("user_id", "0") ?: "0"
    }

    fun sendMessage() {
        if (messageText.value.isBlank() || senderId == "0") return

        // This matches the Flutter code's hardcoded receiver "2"
        val receiverId = "2"
        val messageContent = messageText.value
        messageText.value = "" // Clear the text field

        viewModelScope.launch {
            try {
                val request = SendMessageRequest(
                    senderId = senderId,
                    receiverId = receiverId,
                    message = messageContent
                )
                val response = RetrofitClient.instance.sendMessage(request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    // Add the successfully sent message to the local list
                    val sentMessage = response.body()!!
                    _messages.value = _messages.value + Message(
                        text = sentMessage.message ?: messageContent,
                        isMe = true,
                        timestamp = sentMessage.timestamp ?: ""
                    )
                } else {
                    // Show an error
                    showToast("Failed to send message: ${response.body()?.message}")
                    messageText.value = messageContent // Restore text on failure
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
                messageText.value = messageContent // Restore text on failure
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}