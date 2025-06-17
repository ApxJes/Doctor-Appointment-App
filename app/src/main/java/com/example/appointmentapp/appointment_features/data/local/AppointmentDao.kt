package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Query("SELECT * FROM appointment_table ORDER BY id ASC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Delete
    suspend fun deleteAppointment(appointment: AppointmentEntity)
}