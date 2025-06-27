package com.example.appointmentapp.appointment_features.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    // Appointment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointmentToLocal(appointment: AppointmentEntity)

    @Query("SELECT * FROM appointment_table ORDER BY id ASC")
    fun getAllAppointmentsFromLocal(): Flow<List<AppointmentEntity>>

    @Delete
    suspend fun deleteAppointmentFromLocal(appointment: AppointmentEntity)

    // Doctors
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctorToLocal(doctor: DoctorEntity)

    @Query("SELECT * FROM doctor_table ORDER BY doctorId ASC")
    fun getAllDoctorsFromLocal(): Flow<List<DoctorEntity>>

    @Delete
    suspend fun deleteDoctorFromLocal(doctor: DoctorEntity)

    // Hospitals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHospitalToLocal(hospital: HospitalsVo)

    @Query("SELECT * FROM hospitals_table ORDER BY id ASC")
    fun getAllHospitalsFromLocal(): Flow<List<HospitalsVo>>

    @Delete
    suspend fun deleteHospitalsFromLocal(hospital: HospitalsVo)

}