package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackPress.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnDentistry.setOnClickListener {
            navigateToCategory("Dentist")
        }

        binding.btnCardiologist.setOnClickListener {
            navigateToCategory("Cardiologist")
        }

        binding.btnPulmonologist.setOnClickListener {
            navigateToCategory("Pulmonologist")
        }

        binding.btnGeneral.setOnClickListener {
            navigateToCategory("General")
        }

        binding.btnNeurology.setOnClickListener {
            navigateToCategory("Neurologist")
        }

        binding.btnGastroenterology.setOnClickListener {
            navigateToCategory("Gastroenterologist")
        }

        binding.btnLaboratory.setOnClickListener {
            navigateToCategory("Laboratory")
        }

        binding.btnVeccination.setOnClickListener {
            navigateToCategory("Veccination")
        }
    }

    private fun navigateToCategory(category: String) {
        val action = CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}