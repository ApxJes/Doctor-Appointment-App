package com.example.appointmentapp.appointment_features.domain.repository

import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.appointment_features.data.remote.dto.DoctorsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.NearByHospitalOnGoogleMapDto
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import kotlinx.coroutines.flow.Flow

interface DomainRepository {

    suspend fun getNearByHospitalsOnGoogleMap(query: String): Flow<List<NearByHospitalOnGoogleMapDto>>

    suspend fun getDoctors(): Flow<List<DoctorsDto>>

    suspend fun getDoctorDetails(id: String): Flow<DoctorsDto>

    suspend fun getHospitals(): Flow<List<HospitalsDto>>

    suspend fun insertAppointment(appointment: AppointmentEntity)

    suspend fun deleteAppointment(appointment: AppointmentEntity)

    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    suspend fun insertDoctor(doctors: DoctorsVo)

    suspend fun deleteDoctor(doctors: DoctorsVo)

    fun getAllDoctors(): Flow<List<DoctorsVo>>

}