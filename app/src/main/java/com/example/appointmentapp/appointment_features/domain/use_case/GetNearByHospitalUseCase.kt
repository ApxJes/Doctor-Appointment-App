package com.example.appointmentapp.appointment_features.domain.use_case

import com.example.appointmentapp.appointment_features.domain.model.HospitalVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNearByHospitalUseCase @Inject constructor(
    private val repository: DomainRepository
) {
    suspend operator fun invoke(lat: Double, long: Double): Flow<List<HospitalVo>> {
        val query = "hospital near $lat $long"
        return repository.getHospitals(query).map { hospitalDtoList ->
            hospitalDtoList.map { it.toHospitalVo() }

        }
    }
}