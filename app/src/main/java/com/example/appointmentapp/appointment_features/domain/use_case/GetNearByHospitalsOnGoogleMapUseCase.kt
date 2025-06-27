package com.example.appointmentapp.appointment_features.domain.use_case

import com.example.appointmentapp.appointment_features.domain.model.GetNearByHospitalOnGoogleMapVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNearByHospitalsOnGoogleMapUseCase @Inject constructor(
    private val repository: DomainRepository
) {
    suspend operator fun invoke(lat: Double, long: Double): Flow<List<GetNearByHospitalOnGoogleMapVo>> {
        val query = "hospital near $lat $long"
        return repository.getNearByHospitalsOnGoogleMap(query).map { hospitalDtoList ->
            hospitalDtoList.map { it.toHospitalVo() }

        }
    }
}