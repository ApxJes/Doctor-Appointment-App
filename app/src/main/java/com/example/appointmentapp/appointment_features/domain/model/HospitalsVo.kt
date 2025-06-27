package com.example.appointmentapp.appointment_features.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appointmentapp.appointment_features.data.remote.dto.HospitalsDto
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "hospitals_table")
data class HospitalsVo (
    @PrimaryKey val id: String,
    val image: String,
    val name: String,
    val location: String,
    val rating: String
): Parcelable {
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