package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.HospitalVo
import com.example.appointmentapp.appointment_features.domain.use_case.GetNearByHospitalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearByHospitalViewModel @Inject constructor(
    private val getNearByHospitalUseCase: GetNearByHospitalUseCase
): ViewModel() {

    private var _hospital: MutableStateFlow<List<HospitalVo>> = MutableStateFlow<List<HospitalVo>>(emptyList())
    val hospital: StateFlow<List<HospitalVo>> get() = _hospital

    fun fetchHospitals(lat: Double, lon: Double) {
        viewModelScope.launch {
            getNearByHospitalUseCase(lat, lon)
                .catch {
                    _hospital.value = emptyList()
                }.collect { hospitalList ->
                    _hospital.value = hospitalList
                }
        }
    }
}