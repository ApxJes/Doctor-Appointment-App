package com.example.appointmentapp.appointment_features.domain.repository

import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.appointment_features.data.remote.dto.DoctorsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.NearByHospitalOnGoogleMapDto
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import kotlinx.coroutines.flow.Flow

interface DomainRepository {

    suspend fun fetchNearbyHospitalsFromMap(query: String): Flow<List<NearByHospitalOnGoogleMapDto>>

    suspend fun fetchDoctorsFromNetwork(): Flow<List<DoctorsDto>>
    suspend fun fetchDoctorsDetailsFromNetwork(id: String): Flow<DoctorsDto>

    suspend fun fetchHospitalsFromNetwork(): Flow<List<HospitalsDto>>

    suspend fun insertAppointmentToLocal(appointment: AppointmentEntity)
    suspend fun deleteAppointmentFromLocal(appointment: AppointmentEntity)
    fun getAllAppointmentsFromLocal(): Flow<List<AppointmentEntity>>

    suspend fun insertDoctorToLocal(doctors: DoctorsVo)
    suspend fun deleteDoctorFromLocal(doctors: DoctorsVo)
    fun getAllDoctorsFromLocal(): Flow<List<DoctorsVo>>

    suspend fun insertHospitalsToLocal(hospital: HospitalsVo)
    suspend fun deleteHospitalFromLocal(hospital: HospitalsVo)
    fun getAllHospitalsFromLocal(): Flow<List<HospitalsVo>>

}