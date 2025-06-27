package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo

@Database(entities = [AppointmentEntity::class, DoctorEntity::class, HospitalsVo::class], version = 4, exportSchema = true)
abstract class AppointmentDatabase: RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao
}