package com.example.sparkapp.ui.screens.referral

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sparkapp.network.GenericResponse
import com.example.sparkapp.network.ReferralRequest
import com.example.sparkapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// This mirrors the Flutter logic of showing a dialog/snackbar
enum class SubmissionStatus { IDLE, LOADING, SUCCESS, ERROR }

class ReferralViewModel : ViewModel() {

    // State for all 9 text fields
    val name = mutableStateOf("")
    val age = mutableStateOf("")
    val standard = mutableStateOf("")
    val address = mutableStateOf("")
    val reason = mutableStateOf("")
    val behavior = mutableStateOf("")
    val academic = mutableStateOf("")
    val disciplinary = mutableStateOf("")
    val specialNeed = mutableStateOf("")

    private val _submissionStatus = MutableStateFlow(SubmissionStatus.IDLE)
    val submissionStatus: StateFlow<SubmissionStatus> = _submissionStatus

    fun sendReferral() {
        viewModelScope.launch {
            _submissionStatus.value = SubmissionStatus.LOADING
            try {
                val request = ReferralRequest(
                    name = name.value.trim(),
                    age = age.value.trim().toIntOrNull() ?: 0, // Matches int.tryParse
                    standard = standard.value.trim(),
                    address = address.value.trim(),
                    reason = reason.value.trim(),
                    behavior = behavior.value.trim(),
                    academic = academic.value.trim(),
                    disciplinary = disciplinary.value.trim(),
                    specialNeed = specialNeed.value.trim()
                )

                val response = RetrofitClient.instance.submitReferral(request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    _submissionStatus.value = SubmissionStatus.SUCCESS
                    clearFields() // Clear fields on success
                } else {
                    _submissionStatus.value = SubmissionStatus.ERROR
                }
            } catch (e: Exception) {
                _submissionStatus.value = SubmissionStatus.ERROR
            }
        }
    }

    // Called after the success dialog is dismissed
    fun clearFields() {
        name.value = ""
        age.value = ""
        standard.value = ""
        address.value = ""
        reason.value = ""
        behavior.value = ""
        academic.value = ""
        disciplinary.value = ""
        specialNeed.value = ""
    }

    // Resets the state to hide the dialog
    fun resetStatus() {
        _submissionStatus.value = SubmissionStatus.IDLE
    }
}