package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.GetNearByHospitalOnGoogleMapVo
import com.example.appointmentapp.appointment_features.domain.use_case.GetNearByHospitalsOnGoogleMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetNearByHospitalsOnGoogleMapViewModel @Inject constructor(
    private val getNearByHospitalUseCase: GetNearByHospitalsOnGoogleMapUseCase
): ViewModel() {

    private var _hospital: MutableStateFlow<List<GetNearByHospitalOnGoogleMapVo>> = MutableStateFlow<List<GetNearByHospitalOnGoogleMapVo>>(emptyList())
    val hospital: StateFlow<List<GetNearByHospitalOnGoogleMapVo>> get() = _hospital

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