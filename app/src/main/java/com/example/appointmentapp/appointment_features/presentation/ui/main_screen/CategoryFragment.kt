package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.appointment_features.presentation.adapter.FavoriteDoctorsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.DoctorsViewModel
import com.example.appointmentapp.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var doctorAdapter: FavoriteDoctorsAdapter
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

        doctorAdapter = FavoriteDoctorsAdapter()
        setDoctorRecyclerView()
        observeLoading()
        filterDoctorByCategory()

        binding.btnBackPress.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        doctorAdapter.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToDoctorDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun filterDoctorByCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(viewModel.doctors, viewModel.isLoading) { doctorList, isLoading ->
                val filterList = doctorList.filter {
                    it.specialized?.contains(args.category) ?: false
                }
                Triple(filterList, isLoading, doctorList)
            }.collectLatest { (filterList, isLoading, _) ->
                doctorAdapter.differ.submitList(filterList)

                val showEmpty = filterList.isEmpty() && !isLoading

                binding.emptyLayout.visibility = if (showEmpty) View.VISIBLE else View.GONE
                binding.rcvCategory.visibility = if (showEmpty) View.GONE else View.VISIBLE
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
