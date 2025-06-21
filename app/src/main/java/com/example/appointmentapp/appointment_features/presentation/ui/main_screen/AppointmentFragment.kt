package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
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
import com.example.appointmentapp.appointment_features.presentation.adapter.DoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.adapter.FragmentPagerAdapter
import com.example.appointmentapp.databinding.FragmentAppointmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentFragment : Fragment(),
    FavoriteDoctorListFragment.OnDoctorClickListener {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagerAdapter: FragmentPagerAdapter
    private lateinit var doctorAdapter: DoctorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerAndTabs()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }

    override fun onDoctorClicked(doctor: DoctorsVo) {
        val action = AppointmentFragmentDirections
            .actionAppointmentFragmentToDoctorDetailsFragment(doctor)
        findNavController().navigate(action)
    }

    @SuppressLint("InflateParams")
    private fun setupViewPagerAndTabs() {
        pagerAdapter = FragmentPagerAdapter(childFragmentManager, lifecycle)
        doctorAdapter = DoctorsAdapter()

        binding.viewPager.adapter = pagerAdapter

        val tabTitles = listOf("Your Schedule", "Your Favorite")

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
}