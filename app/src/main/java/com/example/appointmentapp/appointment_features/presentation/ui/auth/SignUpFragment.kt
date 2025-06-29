package com.example.appointmentapp.appointment_features.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentSingUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var googleSignClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

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

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        setUpOnClickListener()
        restoreButtonState()
        signUpWithGoogle()
    }

    private fun setUpOnClickListener() {
        binding.btnCreateAccount.setOnClickListener {
            requestUserEmailPasswordAndName()
        }

        binding.txvSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSingUpFragmentToLoginFragment())
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            googleSignInLauncher.launch(googleSignClient.signInIntent)
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

        binding.btnProgressBar.visibility = View.VISIBLE
        binding.btnCreateAccount.text = ""
        binding.btnCreateAccount.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                val user = auth.currentUser

                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
                user?.updateProfile(profileUpdate)?.await()

                withContext(Dispatchers.Main) {
                    restoreButtonState()
                    Toast.makeText(
                        requireContext(),
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(SignUpFragmentDirections.actionSingUpFragmentToAccountSetUpFragment2())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    restoreButtonState()
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

    private fun restoreButtonState() {
        binding.btnProgressBar.visibility = View.GONE
        binding.btnCreateAccount.text = "Create Account"
        binding.btnCreateAccount.isEnabled = true
    }

    private fun signUpWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("621884972092-tep9227o6v5gtal9aha8miflhl9k2bm2.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        binding.btnProgressBar.visibility = View.VISIBLE
        binding.btnCreateAccount.text = ""
        binding.btnCreateAccount.isEnabled = false

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                restoreButtonState()
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Welcome, ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(SignUpFragmentDirections.actionSingUpFragmentToAccountSetUpFragment2())
                }else {
                    Toast.makeText(requireContext(), "Firebase authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}