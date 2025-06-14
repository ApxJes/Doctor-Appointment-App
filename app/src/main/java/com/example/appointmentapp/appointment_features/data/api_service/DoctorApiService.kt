package com.example.appointmentapp.appointment_features.data.api_service

import com.example.appointmentapp.appointment_features.data.remote.dto.DoctorsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DoctorApiService {

    @GET("api/v1/doctors/Doctors")
    suspend fun getDoctors(): List<DoctorsDto>

    @GET("api/v1/doctors/Doctors/{id}")
    suspend fun getDoctorDetails(
        @Path("id") id: String
    ): DoctorsDto
}