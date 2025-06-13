package com.example.appointmentapp.appointment_features.data.repository

import com.example.appointmentapp.appointment_features.data.remote.MapApiService
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalDto
import com.example.appointmentapp.appointment_features.domain.repository.HospitalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val api: MapApiService
): HospitalRepository {
    override suspend fun getHospitals(query: String): Flow<List<HospitalDto>> = flow {
        emit(api.getNearByHospital(query))
    }.catch {
        emit(emptyList())
    }

}