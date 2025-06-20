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
import com.example.appointmentapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setUpClickListener()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }

    private fun setUpClickListener() {

        binding.btnSignIn.setOnClickListener {
            requestUserEmailAndPassword()
        }

        binding.txvSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSingUpFragment())
        }

        binding.txvForgetPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment())
        }
    }

    private fun requestUserEmailAndPassword() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (!checkValidEmailAndPassword(email, password)) {
            Toast.makeText(
                requireContext(),
                "Please fill out all fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Login successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkValidEmailAndPassword(email: String, password: String): Boolean {
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