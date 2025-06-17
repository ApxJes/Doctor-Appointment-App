package com.example.appointmentapp.appointment_features.data.repository

import com.example.appointmentapp.appointment_features.data.api_service.DoctorApiService
import com.example.appointmentapp.appointment_features.data.api_service.MapApiService
import com.example.appointmentapp.appointment_features.data.local.AppointmentDao
import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.appointment_features.data.remote.dto.DoctorsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalDto
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val api: MapApiService,
    private val doctorApi: DoctorApiService,
    private val dao: AppointmentDao
): DomainRepository {

    override suspend fun getHospitals(query: String): Flow<List<HospitalDto>> = flow {
        emit(api.getNearByHospital(query))
    }.catch {
        emit(emptyList())
    }

    override suspend fun getDoctors(): Flow<List<DoctorsDto>> = flow {
        emit(doctorApi.getDoctors())
    }.catch {
        emit(emptyList())
    }

    override suspend fun getDoctorDetails(id: String): Flow<DoctorsDto> = flow {
        emit(doctorApi.getDoctorDetails(id))
    }

    override suspend fun insertAppointment(appointment: AppointmentEntity) {
        return dao.insertAppointment(appointment)
    }

    override suspend fun deleteAppointment(appointment: AppointmentEntity) {
        return dao.deleteAppointment(appointment)
    }

    override fun getAllAppointments(): Flow<List<AppointmentEntity>> {
        return dao.getAllAppointments()
    }
}