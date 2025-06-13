package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.use_case.GetDoctorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorsViewModel @Inject constructor(
    private val getDoctorsUseCase: GetDoctorsUseCase
) : ViewModel() {

    private var _doctors: MutableStateFlow<List<DoctorsVo>> =
        MutableStateFlow<List<DoctorsVo>>(emptyList())
    val doctors: StateFlow<List<DoctorsVo>> = _doctors

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getDoctors()
    }

    fun getDoctors() {
        viewModelScope.launch {
            getDoctorsUseCase()
                .onStart {
                    _isLoading.value = true
                }
                .catch {
                    _doctors.value = emptyList()
                    _isLoading.value = false
                }
                .collect {
                    _doctors.value = it
                    _isLoading.value = false
                }
        }
    }
}