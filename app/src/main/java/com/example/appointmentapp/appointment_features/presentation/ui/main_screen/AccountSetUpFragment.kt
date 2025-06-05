package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentAccountSetUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSetUpFragment : Fragment() {

    private var _binding: FragmentAccountSetUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSetUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.edtName.setText(currentUser?.displayName ?: "No Name")
        binding.txvEmail.text = currentUser?.email ?: "No Email"

        binding.btnSave.setOnClickListener {
            val newName = binding.edtName.text.toString().trim()
            if (newName.isNotEmpty()) {
                updateUserName(newName)
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserName(newName: String) {
        val user = auth.currentUser
        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Name updated successfully", Toast.LENGTH_SHORT).show()
                        binding.edtName.setText(newName)
                    } else {
                        Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    @SuppressLint("ResourceType")
    override fun onResume() {
        super.onResume()

        val language = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_items, language)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
