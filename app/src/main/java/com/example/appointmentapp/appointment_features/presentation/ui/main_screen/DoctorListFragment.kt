package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.presentation.adapter.DoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.DoctorsViewModel
import com.example.appointmentapp.databinding.FragmentDoctorListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DoctorListFragment : Fragment() {

    private var _binding: FragmentDoctorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var doctorAdapter: DoctorsAdapter
    private val doctorViewModel: DoctorsViewModel by viewModels()
    private var allDoctors: List<DoctorsVo> = emptyList()
    private var selectedButton: AppCompatButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSpecializationButtons()
        observeDoctors()
        observeLoading()

        doctorViewModel.getDoctors()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        doctorAdapter = DoctorsAdapter()
        binding.rcvDoctors.apply {
            adapter = doctorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSpecializationButtons() {
        val buttonMap = mapOf(
            binding.btnAll to "All",
            binding.btnDentistry to "Dentist",
            binding.btnCardiologist to "Cardiologist",
            binding.btnPulmonologist to "Pulmonologist",
            binding.btnGeneral to "General Physician",
            binding.btnNeurology to "Neurologist",
            binding.btnGastroenterology to "Gastroenterologist",
            binding.btnLaboratory to "Laboratory",
            binding.btnVeccination to "Veccinatiologist"
        )

        for((button, specialization) in buttonMap) {
            button.setOnClickListener {
                updateSelectedButton(button)
                filterDoctors(specialization)
            }
        }

        updateSelectedButton(binding.btnAll)
    }

    private fun observeDoctors() {
        lifecycleScope.launchWhenStarted {
            doctorViewModel.doctors.collectLatest { doctorList ->
                allDoctors = doctorList
                filterDoctors("All")
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

    @SuppressLint("SetTextI18n")
    private fun filterDoctors(specialization: String) {
        val filtered = if (specialization == "All") {
            allDoctors
        } else {
            allDoctors.filter { it.specialized.equals(specialization, ignoreCase = true) }
        }

        doctorAdapter.differ.submitList(filtered)
        binding.txvDoctorsCount.text = "${filtered.size} Founds"
        binding.txvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateSelectedButton(newSelected: AppCompatButton) {
        selectedButton?.setBackgroundResource(R.drawable.auth_bg)
        selectedButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        newSelected.setBackgroundResource(R.drawable.category_bg)
        newSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        selectedButton = newSelected
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
