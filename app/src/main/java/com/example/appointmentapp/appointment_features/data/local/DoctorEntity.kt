package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo

@Entity(tableName = "doctor_table")
data class DoctorEntity(

    val about: String?,
    val experience: String?,
    val hospital: String?,
    @PrimaryKey val doctorId: String,
    val name: String?,
    val patients: String?,
    val picture: String?,
    val rating: String?,
    val specialized: String?,
    val workTime: String?,
){
    fun toDoctorsVo(): DoctorsVo = DoctorsVo(
        about, experience, hospital, doctorId, name, patients, picture, rating, specialized, workTime
    )
}
