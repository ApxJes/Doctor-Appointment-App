package com.example.appointmentapp.appointment_features.presentation.ui.onBoarding

import android.content.Context

object OnBoardingPreference {
    private const val PREF_NAME = "onBoarding"
    private const val KEY_IS_FINISHED = "isFinished"

    fun setOnboardingFinished(context: Context, isFinished: Boolean) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(KEY_IS_FINISHED, isFinished).commit()
    }

    fun isOnboardingFinished(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_IS_FINISHED, false)
    }
}