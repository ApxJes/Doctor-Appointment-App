package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.presentation.adapter.DoctorAndHospitalPagerAdapter
import com.example.appointmentapp.appointment_features.presentation.ui.main_screen.FavoriteDoctorListFragment.OnDoctorClickListener
import com.example.appointmentapp.databinding.FragmentFavoriteHospitalBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(),FavoriteDoctorListFragment.OnDoctorClickListener {
    private var _binding: FragmentFavoriteHospitalBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: DoctorAndHospitalPagerAdapter
    private var listener: OnDoctorClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteHospitalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPagerAndTabs()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("InflateParams")
    private fun setupViewPagerAndTabs() {
        pagerAdapter = DoctorAndHospitalPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = pagerAdapter

        val tabTitles = listOf("Doctors", "Hospitals")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = LayoutInflater.from(requireContext())
                .inflate(R.layout.tab_item, null)

            tabView.findViewById<TextView>(R.id.tabText).text = tabTitles[position]
            tab.customView = tabView
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDoctorClicked(doctor: DoctorsVo) {
        val action = FavoriteFragmentDirections
            .actionFavoriteHospitalFragmentToDoctorDetailsFragment(doctor)
        findNavController().navigate(action)
    }
}