package com.example.appointmentapp.appointment_features.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appointmentapp.appointment_features.presentation.ui.main_screen.FavoriteDoctorListFragment
import com.example.appointmentapp.appointment_features.presentation.ui.main_screen.FavoriteFragment
import com.example.appointmentapp.appointment_features.presentation.ui.main_screen.FavoriteHospitalListFragment

class DoctorAndHospitalPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) {
            FavoriteDoctorListFragment()
        } else {
            FavoriteHospitalListFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}