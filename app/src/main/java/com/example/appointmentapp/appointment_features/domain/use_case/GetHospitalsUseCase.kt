package com.example.appointmentapp.appointment_features.domain.use_case

import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHospitalsUseCase @Inject constructor(
    private val repository: DomainRepository
) {

    suspend operator fun invoke(): Flow<List<HospitalsVo>> {
        return repository.getHospitals().map { hospitalsDtoList ->
            hospitalsDtoList.map { hospitalsDto ->
                hospitalsDto.toHospitalsVo()
            }
        }
    }
}