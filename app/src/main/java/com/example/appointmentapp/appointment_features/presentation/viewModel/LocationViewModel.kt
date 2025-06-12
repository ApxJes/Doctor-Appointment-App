package com.example.appointmentapp.appointment_features.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(): ViewModel() {
    private var _locationText: MutableLiveData<String> = MutableLiveData<String>()
    val locationText: LiveData<String> get() = _locationText

    fun updateLocation(city: String, country: String) {
        _locationText.value = "$city, $country"
    }
}