package com.example.appointmentapp.appointment_features.data.di

import android.content.Context
import androidx.room.Room
import com.example.appointmentapp.appointment_features.data.local.AppointmentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providesDatabaseInstance(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppointmentDatabase::class.java,
        "appointment_db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesDao(db: AppointmentDatabase) = db.appointmentDao()
}