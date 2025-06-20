package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.txvUserName.text = auth.currentUser?.displayName ?: "No Name"
        binding.userEmail.text = auth.currentUser?.email ?: "No Name"
        binding.imvUserImage.setImageURI(auth.currentUser?.photoUrl)

        binding.Logout.setOnClickListener {
            setLogOut()
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAccountSetUpFragment())
        }
    }

    @SuppressLint("InflateParams")
    private fun setLogOut() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnYesLogOut = dialogView.findViewById<Button>(R.id.btnConfirm)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnYesLogOut.setOnClickListener {
            alertDialog.dismiss()
            auth.signOut()

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment(), navOptions)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}