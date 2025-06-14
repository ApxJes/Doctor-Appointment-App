package com.example.appointmentapp.appointment_features.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorsVo(
    val about: String?,
    val experience: String?,
    val hospital: String?,
    val id: String?,
    val name: String?,
    val patients: String?,
    val picture: String?,
    val rating: String?,
    val specialized: String?,
    val workTime: String?
): Parcelable
