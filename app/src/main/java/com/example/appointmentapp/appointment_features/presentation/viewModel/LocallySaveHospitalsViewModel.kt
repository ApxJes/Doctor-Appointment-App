package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocallySaveHospitalsViewModel @Inject constructor(
    private val repository: DomainRepository
): ViewModel() {

    private var _isSavedHospital: StateFlow<List<HospitalsVo>> =
        repository.getAllHospitalsFromLocal()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val state: StateFlow<List<HospitalsVo>> get() = _isSavedHospital

    fun isHospitalSaved(hospital: HospitalsVo): Boolean {
        return _isSavedHospital.value.any {
            it.id == hospital.id
        }
    }

    fun toggleHospitalSave(hospital: HospitalsVo) {
        viewModelScope.launch {
            val isSaved = _isSavedHospital.value.any {
                it.id == hospital.id
            }

            if(isSaved) {
                repository.deleteHospitalFromLocal(hospital)
            } else {
                repository.insertHospitalsToLocal(hospital)
            }
        }
    }
}