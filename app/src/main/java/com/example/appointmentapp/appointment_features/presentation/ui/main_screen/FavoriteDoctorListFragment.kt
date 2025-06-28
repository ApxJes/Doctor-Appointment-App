package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.presentation.adapter.FavoriteDoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocallySaveDoctorsViewModel
import com.example.appointmentapp.databinding.FragmentFavoriteDoctorListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteDoctorListFragment : Fragment() {
    private var _binding: FragmentFavoriteDoctorListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocallySaveDoctorsViewModel by viewModels()
    private lateinit var doctorAdapter: FavoriteDoctorsAdapter

    private var listener: OnDoctorClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnDoctorClickListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteDoctorListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorAdapter = FavoriteDoctorsAdapter()

        doctorAdapter.setOnClickListener { doctor ->
            listener?.onDoctorClicked(doctor)
        }

        setRecyclerViewForSavedDoctors()
        observeSaveDoctors()
    }

    private fun observeSaveDoctors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { saveDoctors ->

                    if(saveDoctors.isEmpty()) {
                        binding.rcvDoctorList.visibility = View.INVISIBLE
                        binding.emptyLayout.visibility = View.VISIBLE
                    } else {
                        binding.rcvDoctorList.visibility = View.VISIBLE
                        binding.emptyLayout.visibility = View.GONE
                        doctorAdapter.differ.submitList(saveDoctors)
                    }
                }
            }
        }
    }

    private fun setRecyclerViewForSavedDoctors() {
        binding.rcvDoctorList.apply {
            adapter = doctorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnDoctorClickListener {
        fun onDoctorClicked(doctor: DoctorsVo)
    }
}