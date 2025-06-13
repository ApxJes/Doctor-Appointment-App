package com.example.appointmentapp.appointment_features.data.remote

import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApiService {

    @GET("search")
    suspend fun getNearByHospital(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): List<HospitalDto>
}