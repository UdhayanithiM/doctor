package com.example.sparkapp.network

import com.example.sparkapp.network.GenericResponse
import com.example.sparkapp.network.PostTestRequest
import com.example.sparkapp.network.ScenarioRequest
import com.example.sparkapp.network.TestStatusResponse
import com.example.sparkapp.data.Message
import com.example.sparkapp.network.SendMessageRequest
import com.example.sparkapp.network.SendMessageResponse
import com.example.sparkapp.network.ProfileResponse
import com.example.sparkapp.network.ReferralResponse
import com.example.sparkapp.network.MessageResponse
import com.example.sparkapp.network.ScoreboardResponse
import com.example.sparkapp.network.ParentProfileResponse // <-- ADDED IMPORT
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
    suspend fun getProfile(@Query("email") email: String): Response<ProfileResponse> // <-- Use ProfileResponse

    @POST("profile.php")
    suspend fun updateProfile(@Body data: Map<String, String>): Response<Map<String, String>>

    // --- THIS FUNCTION IS UPDATED ---
    @POST("parentprofile.php")
    suspend fun getParentProfile(@Body request: Map<String, Int>): Response<ParentProfileResponse>

    // --- Counselor (`refferal.php`, `score.php`, `response.php`, `knowledge_response.php`, `check_completion.php`) ---
    @POST("refferal.php") // Matches the Flutter file's URL
    suspend fun submitReferral(@Body request: ReferralRequest): Response<GenericResponse> // We can make a Referral data class later

    @POST("score.php")
    suspend fun submitPreTestScore(@Body data: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, String>>

    @POST("send.php")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<SendMessageResponse>

    // Updated function
    @POST("response.php")
    suspend fun submitScenarioResponse(
        @Body request: ScenarioRequest
    ): Response<GenericResponse>

    // Updated function
    @POST("knowledge_response.php")
    suspend fun submitPostTest(@Body request: PostTestRequest): Response<GenericResponse>

    // Updated function
    @GET("check_completion.php")
    suspend fun checkTestStatus(
        @Query("test_type") testType: String,
        @Query("user_key") userKey: String
    ): Response<TestStatusResponse>

    // --- Counselor (`score_display.php`) ---
    @GET("score_display.php")
    suspend fun getScoreHistory(): Response<ScoreHistoryResponse>

    // --- Doctor (`doc_referal.php`, `get_score.php`) ---
    @GET("doc_referal.php")
    suspend fun getDoctorReferrals(): Response<List<ReferralResponse>> // <-- UPDATED

    @GET("get_score.php")
    suspend fun getScoreboard(): Response<List<ScoreboardResponse>> // <-- UPDATED

    // --- Chat (`get_message.php`) ---
    @GET("get_message.php")
    suspend fun getMessages(
        @Query("sender_id") senderId: String,
        @Query("receiver_id") receiverId: String
    ): Response<List<MessageResponse>> // <-- UPDATED

}