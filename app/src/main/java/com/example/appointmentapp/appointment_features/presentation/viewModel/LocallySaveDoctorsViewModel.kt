package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocallySaveDoctorsViewModel @Inject constructor(
    private val repository: DomainRepository
): ViewModel() {

    private val _isSaveDoctors: StateFlow<List<DoctorsVo>> = repository.getAllDoctorsFromLocal()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val state get() = _isSaveDoctors

    fun toggleDoctorsSave(doctor: DoctorsVo) {
        viewModelScope.launch {
            val isSaved = _isSaveDoctors.value.any { it.id == doctor.id }
            if(isSaved) {
                repository.deleteDoctorFromLocal(doctor)
            } else {
                repository.insertDoctorToLocal(doctor)
            }
        }
    }

    fun isDoctorSaved(product: DoctorsVo): Boolean {
        return _isSaveDoctors.value.any { it.id == product.id }
    }
}