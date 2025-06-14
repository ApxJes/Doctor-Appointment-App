package com.example.appointmentapp.appointment_features.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.use_case.GetDoctorDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorDetailsViewModel @Inject constructor(
    private val doctorDetailsUseCase: GetDoctorDetailsUseCase
) : ViewModel() {

    private val _doctorDetails = MutableStateFlow<DoctorsVo?>(null)
    val doctorDetails: StateFlow<DoctorsVo?> = _doctorDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchDoctorDetailsById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                doctorDetailsUseCase(id).collect { result ->
                    _doctorDetails.value = result
                }
            } catch (e: Exception) {
                Log.e("DoctorDetailsViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
