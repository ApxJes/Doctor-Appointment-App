package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentAccountSetUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AccountSetUpFragment : Fragment() {
    private var _bindng: FragmentAccountSetUpBinding? = null
    private val binding get() = _bindng!!

    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var firestore: FirebaseFirestore

    private var selectImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectImageUri = it
                binding.imgProfile.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindng = FragmentAccountSetUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        checkLoginState()

        binding.BirthInputLayout.setOnClickListener {
            showDatePicker()
        }

        binding.edtBirthDate.isFocusable = false
        binding.edtBirthDate.isClickable = false

        binding.btnSave.setOnClickListener {
            val newName = binding.edtName.text.toString().trim()
            if(!newName.isEmpty()) {
                updateUserName(newName)
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }

            val nickname = binding.edtNickName.text.toString().trim()
            val dob = binding.edtBirthDate.text.toString().trim()
            val gender = binding.edtGender.text.toString().trim()

            saveUserProfileData(nickname = nickname, dob = dob, gender = gender)

            setDialogBoxToNavigateToHome()
        }

        binding.imvUploadProfile.setOnClickListener {
            imagePickerLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun updateUserName(newName: String) {
        val userName = auth.currentUser
        userName?.let {
            val builder = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)

            selectImageUri?.let {
                builder.setPhotoUri(it)
            }

            val profileUpdate = builder.build()

            it.updateProfile(profileUpdate)
                .addOnCompleteListener {  task ->
                    if(task.isSuccessful) {
                        binding.edtName.setText(newName)
                    } else {
                        Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveUserProfileData(nickname: String, dob: String, gender: String) {
        val userId = auth.currentUser?.uid ?: return

        val userData = hashMapOf(
            "nickname" to nickname,
            "dateOfBirth" to dob,
            "gender" to gender
        )

        firestore.collection("users")
            .document(userId)
            .set(userData)
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkLoginState() {
        val currentUser = auth.currentUser
        currentUser.let {
            binding.edtName.setText(currentUser?.displayName.orEmpty())
            binding.txvEmail.text = currentUser?.email.orEmpty()

            if(currentUser?.photoUrl != null) {
                try {
                    binding.imgProfile.setImageURI(currentUser.photoUrl)
                } catch (e: SecurityException) {
                    binding.imgProfile.setImageResource(R.drawable.user_pf)
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.user_pf)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.edtBirthDate.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }



    private fun setDialogBoxToNavigateToHome() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.account_setup_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
            val action = AccountSetUpFragmentDirections.actionAccountSetUpFragment2ToHomeFragment()
            findNavController().navigate(action)
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}