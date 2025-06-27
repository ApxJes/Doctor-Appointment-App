package com.example.appointmentapp.appointment_features.data.remote.dto


import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import com.google.gson.annotations.SerializedName

data class HospitalsDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("rating")
    val rating: String?
) {
    fun toHospitalsVo(): HospitalsVo {
        return HospitalsVo(
            id = id ?: "0",
            image = image ?: "",
            location = location ?: "",
            name = name ?: "",
            rating = rating ?: ""
        )
    }
}