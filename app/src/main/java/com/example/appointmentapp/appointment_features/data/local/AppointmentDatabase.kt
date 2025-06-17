package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppointmentEntity::class], version = 2, exportSchema = true)
abstract class AppointmentDatabase: RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao
}