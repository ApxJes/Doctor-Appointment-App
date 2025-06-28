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
import com.example.appointmentapp.appointment_features.presentation.adapter.FavoriteHospitalsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocallySaveHospitalsViewModel
import com.example.appointmentapp.databinding.FragmentFavoriteHospitalBinding
import com.example.appointmentapp.databinding.FragmentFavoriteHospitalListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoriteHospitalListFragment : Fragment() {
    private var _binding: FragmentFavoriteHospitalListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocallySaveHospitalsViewModel by viewModels()
    private lateinit var savedHospitalsAdapter: FavoriteHospitalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteHospitalListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedHospitalsAdapter = FavoriteHospitalsAdapter(
            onSaveClick = { hospital ->
                viewModel.toggleHospitalSave(hospital)
            },
            isHospitalSaved = { hospital ->
                viewModel.isHospitalSaved(hospital)
            }
        )

        setUpHospitalsListRecyclerView()
        fetchSavedHospitalsFromLocal()
    }

    private fun fetchSavedHospitalsFromLocal() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest {
                if(it.isEmpty()) {
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.rcvHospitalList.visibility = View.INVISIBLE
                } else {
                    binding.emptyLayout.visibility = View.GONE
                    binding.rcvHospitalList.visibility = View.VISIBLE
                    savedHospitalsAdapter.differ.submitList(it)
                }
            }
        }
    }

    private fun setUpHospitalsListRecyclerView() {
        binding.rcvHospitalList.apply {
            adapter = savedHospitalsAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}