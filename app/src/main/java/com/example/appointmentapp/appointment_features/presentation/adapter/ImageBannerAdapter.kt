package com.example.appointmentapp.appointment_features.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appointmentapp.appointment_features.presentation.ui.main_screen.ImageBannerFragment

class ImageBannerAdapter(
    fragment: Fragment,
    private val imageList: List<Int>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = imageList.size

    override fun createFragment(position: Int): Fragment {
        return ImageBannerFragment.newInstance(imageList[position])
    }
}
