package com.example.appointmentapp.appointment_features.presentation.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.databinding.FragmentSingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSingUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        setUpOnClickListener()
    }

    private fun setUpOnClickListener() {
        binding.btnCreateAccount.setOnClickListener {
            requestUserEmailPasswordAndName()
        }

        binding.txvSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSingUpFragmentToLoginFragment())
        }
    }

    private fun requestUserEmailPasswordAndName() {
        val userName = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (!checkValidUserInfo(userName, email, password)) {
            Toast.makeText(
                requireContext(),
                "Please fill out all fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                val user = auth.currentUser

                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
                user?.updateProfile(profileUpdate)?.await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(SignUpFragmentDirections.actionSingUpFragmentToAccountSetUpFragment2())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        e.message ?: "Registration failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun checkValidUserInfo(userName: String, email: String, password: String): Boolean {
        if(userName.isEmpty()) {
            binding.edtName.error = "User name can't be empty"
            binding.edtName.requestFocus()
            return false
        }

        if(email.isEmpty()) {
            binding.edtEmail.error = "Email can't be empty"
            binding.edtEmail.requestFocus()
            return false
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Please enter a valid email"
            binding.edtEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = "Password can't be empty"
            binding.edtPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.edtPassword.error = "Password must be at least 6 characters"
            binding.edtPassword.requestFocus()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}