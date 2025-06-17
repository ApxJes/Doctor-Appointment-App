package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val repository: DomainRepository
): ViewModel(){

    val allAppointments: StateFlow<List<AppointmentEntity>> =
        repository.getAllAppointments().stateIn(
            viewModelScope, SharingStarted.Lazily, emptyList()
        )

    fun booking(appointment: AppointmentEntity) {
        viewModelScope.launch {
            repository.insertAppointment(appointment)
        }
    }

    fun deleteAppointment(appointment: AppointmentEntity) {
        viewModelScope.launch {
            repository.deleteAppointment(appointment)
        }
    }
}