package com.example.sparkapp.network

import com.squareup.moshi.Json

data class TestStatusResponse(
    @field:Json(name = "status") val status: String, // "completed", "not_completed", or "error"
    @field:Json(name = "message") val message: String?
)