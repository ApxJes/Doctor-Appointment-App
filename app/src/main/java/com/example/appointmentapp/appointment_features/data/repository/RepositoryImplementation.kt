package com.example.appointmentapp.appointment_features.data.repository

import com.example.appointmentapp.appointment_features.data.api_service.AppointmentApiService
import com.example.appointmentapp.appointment_features.data.api_service.MapApiService
import com.example.appointmentapp.appointment_features.data.local.AppointmentDao
import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.appointment_features.data.remote.dto.DoctorsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.NearByHospitalOnGoogleMapDto
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val mapApi: MapApiService,
    private val appointmentApi: AppointmentApiService,
    private val dao: AppointmentDao
): DomainRepository {

    override suspend fun fetchNearbyHospitalsFromMap(query: String): Flow<List<NearByHospitalOnGoogleMapDto>> = flow {
        emit(mapApi.getNearByHospitalONGoogleMap(query))
    }.catch {
        emit(emptyList())
    }

    override suspend fun fetchDoctorsFromNetwork(): Flow<List<DoctorsDto>> = flow {
        emit(appointmentApi.getDoctors())
    }.catch {
        emit(emptyList())
    }

    override suspend fun fetchDoctorsDetailsFromNetwork(id: String): Flow<DoctorsDto> = flow {
        emit(appointmentApi.getDoctorDetails(id))
    }

    override suspend fun fetchHospitalsFromNetwork(): Flow<List<HospitalsDto>> = flow{
        emit(appointmentApi.getHospitals())
    }.catch {
        emit(emptyList())
    }

    override suspend fun insertAppointmentToLocal(appointment: AppointmentEntity) {
        return dao.insertAppointmentToLocal(appointment)
    }

    override suspend fun deleteAppointmentFromLocal(appointment: AppointmentEntity) {
        return dao.deleteAppointmentFromLocal(appointment)
    }

    override fun getAllAppointmentsFromLocal(): Flow<List<AppointmentEntity>> {
        return dao.getAllAppointmentsFromLocal()
    }

    override suspend fun insertDoctorToLocal(doctors: DoctorsVo) {
        return dao.insertDoctorToLocal(doctors.toDoctorEntity())
    }

    override suspend fun deleteDoctorFromLocal(doctors: DoctorsVo) {
        return dao.deleteDoctorFromLocal(doctors.toDoctorEntity())
    }

    override fun getAllDoctorsFromLocal(): Flow<List<DoctorsVo>> {
        return dao.getAllDoctorsFromLocal().map {
            it.map{ it.toDoctorsVo() }
        }
    }

    override suspend fun insertHospitalsToLocal(hospital: HospitalsVo) {
        return dao.insertHospitalToLocal(hospital)
    }

    override suspend fun deleteHospitalFromLocal(hospital: HospitalsVo) {
        return dao.deleteHospitalsFromLocal(hospital)
    }

    override fun getAllHospitalsFromLocal(): Flow<List<HospitalsVo>> {
        return dao.getAllHospitalsFromLocal()
    }
}