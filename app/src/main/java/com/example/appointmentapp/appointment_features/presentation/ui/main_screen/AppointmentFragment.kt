package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.presentation.adapter.DoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.adapter.FragmentPagerAdapter
import com.example.appointmentapp.databinding.FragmentAppointmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentFragment : Fragment(), FavoriteDoctorListFragment.OnDoctorClickListener{
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FragmentPagerAdapter
    private lateinit var doctorAdapter: DoctorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppointmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FragmentPagerAdapter(childFragmentManager, lifecycle)
        doctorAdapter = DoctorsAdapter()
        binding.viewPager.adapter = adapter

        val tabTitles = listOf("Your Schedule", "Your Favorite")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item, null)
            val textView = customView.findViewById<TextView>(R.id.tabText)
            textView.text = tabTitles[position]
            tab.customView = customView
        }.attach()
    }

    override fun onDoctorClicked(doctor: DoctorsVo) {
        val action = AppointmentFragmentDirections
            .actionAppointmentFragmentToDoctorDetailsFragment(doctor)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}