package com.example.appointmentapp.appointment_features.data.api_service

import com.example.appointmentapp.appointment_features.data.remote.dto.DoctorsDto
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AppointmentApiService {

    @GET("api/v1/doctors/Doctors")
    suspend fun getDoctors(): List<DoctorsDto>

    @GET("api/v1/doctors/Doctors/{id}")
    suspend fun getDoctorDetails(
        @Path("id") id: String
    ): DoctorsDto

    @GET("api/v1/doctors/Hospital")
    suspend fun getHospitals(): List<HospitalsDto>

}