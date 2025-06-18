package com.example.appointmentapp.appointment_features.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appointmentapp.appointment_features.data.local.DoctorEntity
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
    val workTime: String?,
): Parcelable {
    fun toDoctorEntity(): DoctorEntity = DoctorEntity(
        about, experience, hospital, id!!, name, patients, picture, rating, specialized, workTime
    )
}
