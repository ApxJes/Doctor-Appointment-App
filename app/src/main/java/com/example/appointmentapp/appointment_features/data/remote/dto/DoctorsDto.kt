package com.example.appointmentapp.appointment_features.data.remote.dto


import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.google.gson.annotations.SerializedName

data class DoctorsDto(
    @SerializedName("about")
    val about: String?,
    @SerializedName("experience")
    val experience: String?,
    @SerializedName("hospital")
    val hospital: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("patients")
    val patients: String?,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("specialized")
    val specialized: String?,
    @SerializedName("work_time")
    val workTime: String?,
) {
    fun toDoctorsVo(): DoctorsVo {
        return DoctorsVo (
            about = about,
            experience = experience,
            hospital = hospital,
            id = id,
            name = name,
            patients = patients,
            picture = picture,
            rating = rating,
            specialized = specialized,
            workTime = workTime,
        )
    }
}