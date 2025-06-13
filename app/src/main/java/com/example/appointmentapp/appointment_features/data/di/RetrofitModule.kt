package com.example.appointmentapp.appointment_features.data.di

import com.example.appointmentapp.appointment_features.data.api_service.DoctorApiService
import com.example.appointmentapp.appointment_features.data.api_service.MapApiService
import com.example.appointmentapp.core.Constants.DOCTORS_BASE_URL
import com.example.appointmentapp.core.Constants.MAP_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    @MapRetrofit
    fun provideMapRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    @DoctorRetrofit
    fun provideDoctorRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DOCTORS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideMapApiService(@MapRetrofit retrofit: Retrofit): MapApiService {
        return retrofit.create(MapApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDoctorApiService(@DoctorRetrofit retrofit: Retrofit): DoctorApiService {
        return retrofit.create(DoctorApiService::class.java)
    }
}
