package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.presentation.adapter.HospitalsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocallySaveHospitalsViewModel
import com.example.appointmentapp.databinding.FragmentFavoriteHospitalBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoriteHospitalFragment : Fragment() {
    private var _binding: FragmentFavoriteHospitalBinding? = null
    private val binding get() = _binding!!
    private lateinit var hospitalsAdapter: HospitalsAdapter
    private val locallySaveHospitalsViewModel: LocallySaveHospitalsViewModel by viewModels()

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
        hospitalsAdapter = HospitalsAdapter(
            onSaveClick = {
                locallySaveHospitalsViewModel.toggleHospitalSave(it)
            },
            isHospitalSaved = {
                locallySaveHospitalsViewModel.isHospitalSaved(it)
            }
        )
        setUpFavoriteHospitalsRecyclerView()
        setUpHospitalObserverFromLocal()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setUpFavoriteHospitalsRecyclerView() {
        binding.rcvFavoriteHospital.apply {
            adapter = hospitalsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpHospitalObserverFromLocal() {
        lifecycleScope.launchWhenStarted {
            locallySaveHospitalsViewModel.state.collectLatest { hospitalList ->
                if (hospitalList.isEmpty()) {
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.rcvFavoriteHospital.visibility = View.INVISIBLE
                } else {
                    binding.emptyLayout.visibility = View.GONE
                    binding.rcvFavoriteHospital.visibility = View.VISIBLE
                    hospitalsAdapter.differ.submitList(hospitalList)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}