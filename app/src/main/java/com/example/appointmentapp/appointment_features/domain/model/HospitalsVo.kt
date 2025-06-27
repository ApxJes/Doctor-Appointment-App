package com.example.appointmentapp.appointment_features.domain.model

import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalsDto

data class HospitalsVo (
    val id: String,
    val image: String,
    val name: String,
    val location: String,
    val rating: String
) {
    fun toHospitalDto(): HospitalsDto {
        return HospitalsDto(
            id = id,
            image = image,
            name = name,
            location = location,
            rating = rating
        )
    }
}