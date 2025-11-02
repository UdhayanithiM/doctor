package com.example.sparkapp.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    @field:Json(name = "sender_id") val senderId: String,
    @field:Json(name = "receiver_id") val receiverId: String,
    @field:Json(name = "message") val message: String
)