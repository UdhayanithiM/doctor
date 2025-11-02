package com.example.sparkapp.network

import com.example.sparkapp.network.GenericResponse
import com.example.sparkapp.network.ScenarioRequest
import com.example.sparkapp.network.TestStatusResponse
import retrofit2.Response
import retrofit2.http.*

// This interface is the "contract" with your PHP backend.
// Every function here matches one of your .php files.
interface ApiService {

    // --- Auth (`login.php`, `signup.php`) ---
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // We use Map<String, Any> for dynamic fields
    @POST("signup.php")
    suspend fun signup(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, String>>

    // --- Profile (`profile.php`, `parentprofile.php`) ---
    @GET("profile.php")
    suspend fun getProfile(@Query("email") email: String): Response<Map<String, Any>> // We can make a Profile data class later

    @POST("profile.php")
    suspend fun updateProfile(@Body data: Map<String, String>): Response<Map<String, String>>

    @POST("parentprofile.php")
    suspend fun getParentProfile(@Body data: Map<String, Int>): Response<Map<String, Any>> // We can make a ParentProfile class later

    // --- Counselor (`refferal.php`, `score.php`, `response.php`, `knowledge_response.php`, `check_completion.php`) ---
    @POST("refferal.php")
    suspend fun submitReferral(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, String>> // We can make a Referral data class later

    @POST("score.php")
    suspend fun submitPreTestScore(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, String>>

    // Updated function
    @POST("response.php")
    suspend fun submitScenarioResponse(
        @Body request: ScenarioRequest
    ): Response<GenericResponse>

    @POST("knowledge_response.php")
    suspend fun submitPostTest(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, String>>

    // Updated function
    @GET("check_completion.php")
    suspend fun checkTestStatus(
        @Query("test_type") testType: String,
        @Query("user_key") userKey: String
    ): Response<TestStatusResponse>

    // --- Doctor (`doc_referal.php`, `get_score.php`) ---
    @GET("doc_referal.php")
    suspend fun getReferrals(): Response<List<Map<String, Any>>> // We can make a Referral data class later

    @GET("get_score.php")
    suspend fun getScenarioScores(): Response<List<Map<String, Any>>>

    // --- Counselor (`score_display.php`) ---
    @GET("score_display.php")
    suspend fun getScoreHistory(): Response<Map<String, Any>>

    // --- Chat (`send.php`, `get_message.php`) ---
    @POST("send.php")
    suspend fun sendMessage(@Body data: Map<String, String>): Response<Map<String, Any>>

    @GET("get_message.php")
    suspend fun getMessages(@Query("receiver_id") receiverId: String): Response<List<Map<String, Any>>>
}