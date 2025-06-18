package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo

@Database(entities = [AppointmentEntity::class, DoctorEntity::class], version = 3, exportSchema = true)
abstract class AppointmentDatabase: RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao
}