package com.example.appointmentapp.appointment_features.presentation.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnReset.setOnClickListener {
            requestUserEmailAddress()
        }
    }

    private fun requestUserEmailAddress() {
        val emailAddress = binding.edtEmail.text.toString()
        if (!checkValidEmail(emailAddress)) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.sendPasswordResetEmail(emailAddress).await()
                withContext(Dispatchers.Main) {
                    findNavController().navigate(
                        ForgetPasswordFragmentDirections
                            .actionForgetPasswordFragmentToLoginFragment()
                    )

                    Toast.makeText(
                        requireContext(),
                        "Please check your email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "We couldn't find your email. Please try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkValidEmail(email: String): Boolean {
        if (email.isEmpty()) {
            binding.edtEmail.error = "Email can't be empty!"
            binding.edtEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Please input valid email"
            binding.edtEmail.requestFocus()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}