package com.example.appointmentapp.appointment_features.domain.repository

import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalDto
import kotlinx.coroutines.flow.Flow

interface HospitalRepository {

    suspend fun getHospitals(query: String): Flow<List<HospitalDto>>
}