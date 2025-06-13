package com.example.appointmentapp.appointment_features.data.di

import com.example.appointmentapp.appointment_features.data.repository.RepositoryImplementation
import com.example.appointmentapp.appointment_features.domain.repository.DomainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun providesRepoImpl(
        hospitalRepositoryImpl: RepositoryImplementation
    ): DomainRepository
}