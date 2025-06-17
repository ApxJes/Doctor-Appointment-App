package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointment_table")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorId: String,
    val doctorName: String,
    val specialization: String,
    val hospital: String,
    val imageUrl: String,
    val selectedDate: String,
    val selectedTime: String
)
