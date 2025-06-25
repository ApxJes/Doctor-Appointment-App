package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentTermsAndConditionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsFragment : Fragment() {

    private var _binding: FragmentTermsAndConditionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbAcceptTerms.setOnCheckedChangeListener { _, isChecked ->
            binding.btnAccept.isEnabled = isChecked

            if (isChecked) {
                binding.btnAccept.text = "Accepted"
                binding.btnAccept.setBackgroundResource(R.drawable.on_boarding_button)
                binding.btnAccept.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            } else {
                binding.btnAccept.text = "Accept"
                binding.btnAccept.setBackgroundResource(R.drawable.disable_button)
                binding.btnAccept.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
            }
        }

        binding.btnAccept.setOnClickListener {
            Toast.makeText(requireContext(), "Thank you for accepting the terms!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
