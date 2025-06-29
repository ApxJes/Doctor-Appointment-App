package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.presentation.adapter.FavoriteDoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.DoctorsViewModel
import com.example.appointmentapp.databinding.FragmentDoctorListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DoctorListFragment : Fragment() {

    private var _binding: FragmentDoctorListBinding? = null
    private val binding get() = _binding!!

    private val doctorViewModel: DoctorsViewModel by viewModels()
    private lateinit var doctorAdapter: FavoriteDoctorsAdapter

    private var allDoctors: List<DoctorsVo> = emptyList()
    private var selectedButton: AppCompatButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        setupRecyclerView()
        setupButtonListeners()
        setupObservers()
        setupNavigation()
        setupSearchListener()
    }

    private fun setupRecyclerView() {
        doctorAdapter = FavoriteDoctorsAdapter().apply {
            setOnClickListener { doctor -> navigateToDoctorDetails(doctor) }
        }
        binding.rcvDoctors.apply {
            adapter = doctorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupButtonListeners() {
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

        buttonMap.forEach { (button, specialization) ->
            button.setOnClickListener {
                updateSelectedButton(button)
                filterDoctorsBySpecializationAndQuery(
                    specialization,
                    binding.edtSearchDoctor.text?.toString().orEmpty().trim()
                )
            }
        }

        updateSelectedButton(binding.btnAll)
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            doctorViewModel.doctors.collectLatest { doctorList ->
                allDoctors = doctorList
                filterDoctorsBySpecializationAndQuery(
                    selectedButton?.text?.toString() ?: "All",
                    binding.edtSearchDoctor.text?.toString().orEmpty().trim()
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            doctorViewModel.isLoading.collect { isLoading -> handleLoadingState(isLoading) }
        }
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupSearchListener() {
        binding.edtSearchDoctor.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val query = charSequence?.toString().orEmpty().trim()
                    filterDoctorsBySpecializationAndQuery(
                        selectedButton?.text?.toString() ?: "All",
                        query
                    )
                }

                override fun afterTextChanged(editable: Editable?) {
                }
            }
        )
    }


    @SuppressLint("SetTextI18n")
    private fun filterDoctorsBySpecializationAndQuery(
        specialization: String,
        query: String
    ) {
        val filteredBySpecialization = if (specialization == "All") {
            allDoctors
        } else {
            allDoctors.filter { it.specialized.equals(specialization) }
        }

        val filteredByQuery = if (query.isEmpty()) {
            filteredBySpecialization
        } else {
            filteredBySpecialization.filter { it.name!!.contains(query) }
        }

        updateDoctorsListUI(filteredByQuery)
    }

    private fun updateDoctorsListUI(doctors: List<DoctorsVo>) {
        doctorAdapter.differ.submitList(doctors)
        binding.txvDoctorsCount.text = "${doctors.size} Found"

        val showEmptyState = doctors.isEmpty() && !doctorViewModel.isLoading.value
        binding.txvEmpty.visibility = if (showEmptyState) View.VISIBLE else View.GONE
        binding.rcvDoctors.visibility = if (showEmptyState) View.GONE else View.VISIBLE
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.progressBarLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.txvEmpty.visibility = View.GONE
            binding.rcvDoctors.visibility = View.GONE
        }
    }

    private fun updateSelectedButton(newButton: AppCompatButton) {
        selectedButton?.apply {
            setBackgroundResource(R.drawable.auth_bg)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        newButton.setBackgroundResource(R.drawable.category_bg)
        newButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        selectedButton = newButton
    }

    private fun navigateToDoctorDetails(doctor: DoctorsVo) {
        val action = DoctorListFragmentDirections
            .actionDoctorListFragmentToDoctorDetailsFragment(doctor)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
