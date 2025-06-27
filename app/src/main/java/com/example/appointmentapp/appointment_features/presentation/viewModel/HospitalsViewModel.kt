package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import com.example.appointmentapp.appointment_features.domain.use_case.GetHospitalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalsViewModel @Inject constructor(
    private val getHospitalsUseCase: GetHospitalsUseCase
): ViewModel() {

    private var _getHospitals: MutableStateFlow<List<HospitalsVo>> = MutableStateFlow<List<HospitalsVo>>(emptyList())
    val getHospitals: StateFlow<List<HospitalsVo>> get() = _getHospitals

    init {
        fetchHospitalsFromApi()
    }

    fun fetchHospitalsFromApi() {
        viewModelScope.launch {
            getHospitalsUseCase()
                .collect {  hospitalVoList ->
                    _getHospitals.value = hospitalVoList
                }
        }
    }
}