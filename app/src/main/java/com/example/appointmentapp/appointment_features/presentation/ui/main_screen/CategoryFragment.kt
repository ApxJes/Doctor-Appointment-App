package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.appointment_features.presentation.adapter.DoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.DoctorsViewModel
import com.example.appointmentapp.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var doctorAdapter: DoctorsAdapter
    private val viewModel: DoctorsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = args.category
        binding.txvTitle.text = category

        doctorAdapter = DoctorsAdapter()
        setDoctorRecyclerView()
        observeLoading()
        filterDoctorByCategory()

        binding.btnBackPress.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun filterDoctorByCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.doctors.collectLatest { doctorList ->
                val filterList = doctorList.filter {
                    it.specialized!!.contains(args.category)
                }

                doctorAdapter.differ.submitList(filterList)

                if (filterList.isEmpty() && viewModel.isLoading.value == false) {
                    binding.txvEmpty.visibility = View.VISIBLE
                    binding.rcvCategory.visibility = View.GONE

                } else {
                    binding.txvEmpty.visibility = View.GONE
                    binding.rcvCategory.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setDoctorRecyclerView() {
        binding.rcvCategory.apply {
            adapter = doctorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLoading() {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBarLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
