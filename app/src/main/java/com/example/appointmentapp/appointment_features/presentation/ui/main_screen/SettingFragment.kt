package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentSettingBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonOnClickListener()
    }

    private fun setButtonOnClickListener() {
        binding.changePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        binding.privacyPolicy.setOnClickListener {
            openWebPage("https://yourdomain.com/privacy-policy")
        }

        binding.about.setOnClickListener {
            showAboutDialog()
        }
    }

    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)
        val oldPass: EditText = dialogView.findViewById(R.id.edtOldPassword)
        val newPass: EditText = dialogView.findViewById(R.id.edtNewPassword)

        AlertDialog.Builder(requireContext())
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change") { _, _ ->
                val oldPassword = oldPass.text.toString()
                val newPassword = newPass.text.toString()
                handlePasswordChange(oldPassword, newPassword)
            }
    }

    private fun handlePasswordChange(oldPassword: String, newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            user.reauthenticate(credential).addOnCompleteListener { authTask ->
                if(authTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Password update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Authentication failed. Check old password.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("About")
            .setMessage("Doctor Appointment App v1.0\nDeveloped by Your Name.\n© 2025 All rights reserved.")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}