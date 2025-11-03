package com.example.sparkapp.network

import com.squareup.moshi.Json

// Data class for doc_referal.php
data class ReferralResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "age") val age: String?,
    @field:Json(name = "standard") val standard: String?,
    @field:Json(name = "performance") val performance: String?, // Note: Flutter code has this, but API doesn't. Added for compatibility.
    @field:Json(name = "behavior") val behavior: String? // Note: Flutter code has this, but API doesn't. Added for compatibility.
)

// Data class for get_message.php
data class MessageResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "sender_id") val senderId: String?,
    @field:Json(name = "message") val message: String?
)

// Data class for get_score.php
data class ScoreboardResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "username") val username: String?,
    @field:Json(name = "scenario") val scenario: String?,
    @field:Json(name = "answer1") val answer1: String?,
    @field:Json(name = "answer2") val answer2: String?,
    @field:Json(name = "answer3") val answer3: String?,
    @field:Json(name = "answer4") val answer4: String?,
    @field:Json(name = "score") val score: String?
)