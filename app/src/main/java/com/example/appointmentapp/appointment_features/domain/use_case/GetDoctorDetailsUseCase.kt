package com.example.appointmentapp.appointment_features.domain.use_case

import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDoctorDetailsUseCase @Inject constructor(
    private val repository: DomainRepository
) {

    suspend operator fun invoke(id: String): Flow<DoctorsVo> {
        return repository.getDoctorDetails(id).map {
            it.toDoctorsVo()
        }
    }
}