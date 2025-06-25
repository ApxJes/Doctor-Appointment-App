package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentHelpAndSupportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpAndSupportFragment : Fragment() {
    private var _binding: FragmentHelpAndSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpAndSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendFeedback.setOnClickListener {
            sendEmailFeedback()
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun sendEmailFeedback() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("aungkyae32@gamil.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for Doctor Appointment App")
            putExtra(Intent.EXTRA_TEXT, "Please write your feedback here...")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send feedback using"))
        }catch (e: android.content.ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}