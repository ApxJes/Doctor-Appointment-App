package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.presentation.viewModel.DoctorDetailsViewModel
import com.example.appointmentapp.appointment_features.presentation.viewModel.DoctorsViewModel
import com.example.appointmentapp.databinding.FragmentDoctorDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DoctorDetailsFragment : Fragment() {
    private var _binding: FragmentDoctorDetailsBinding? = null
    private val binding get() = _binding!!
    private val doctorDetailsViewModel: DoctorDetailsViewModel by viewModels()
    private val doctorViewModel: DoctorsViewModel by viewModels()
    private val args: DoctorDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doctorId = args.doctors.id ?: return

        doctorDetailsViewModel.fetchDoctorDetailsById(doctorId)
        getProductDetails()
        observeLoading()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnBookAppointment.setOnClickListener {
            val doctor = args.doctors
            val action = DoctorDetailsFragmentDirections.actionDoctorDetailsFragmentToBookAppointmentFragment(doctor)
            findNavController().navigate(action)
        }
    }

    private fun getProductDetails() {
        lifecycleScope.launchWhenStarted {
            doctorDetailsViewModel.doctorDetails.collectLatest { doctor ->
                binding.txvDoctorName.text = doctor?.name
                binding.txvSpecialized.text = doctor?.specialized
                binding.txvHospitalAddress.text = doctor?.hospital
                binding.txvPatientsCount.text = "${doctor?.patients}+"
                binding.txvExperience.text = doctor?.experience
                binding.txvRating.text = doctor?.rating
                binding.txvAboutMe.text = doctor?.about
                binding.txvWorkingTime.text = "Monday-Friday, ${doctor?.workTime}"
                Glide.with(requireContext()).load(doctor?.picture).into(binding.imvDoctor)
            }
        }
    }

    private fun observeLoading() {
        lifecycleScope.launchWhenStarted {
            doctorViewModel.isLoading.collect { isLoading ->
                binding.progressBarLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}