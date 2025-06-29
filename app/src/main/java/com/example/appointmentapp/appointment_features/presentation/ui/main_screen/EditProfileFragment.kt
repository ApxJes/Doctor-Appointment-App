package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
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
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderOptions = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            genderOptions
        )
        binding.edtGender.setAdapter(adapter)

        binding.edtGender.setOnTouchListener { _, _ ->
            binding.edtGender.showDropDown()
            false
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        checkLoginState()
        loadUserProfileData()

        binding.BirthInputLayout.setOnClickListener {
            showDatePicker()
        }

        binding.edtBirthDate.isFocusable = false
        binding.edtBirthDate.isClickable = false


        binding.btnSave.setOnClickListener {
            val newName = binding.edtName.text.toString().trim()
            val newNickname = binding.edtNickName.text.toString().trim()
            val newDob = binding.edtBirthDate.text.toString().trim()
            val newGender = binding.edtGender.text.toString().trim()

            if (newName.isNotEmpty()) {
                updateUserNamePhoto(newName)
                saveUpdatedProfileData(newNickname, newDob, newGender)
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackSpace.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imvUploadProfile.setOnClickListener {
            imagePickerLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun loadUserProfileData() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    binding.edtNickName.setText(document.getString("nickname").orEmpty())
                    binding.edtBirthDate.text = document.getString("dateOfBirth").orEmpty()
                    val gender = document.getString("gender") ?: ""
                    if (gender in listOf("Male", "Female", "Other")) {
                        binding.edtGender.setText(gender, false)
                    } else {
                        binding.edtGender.hint = "Gender"
                        binding.edtGender.setHintTextColor(ActivityCompat.getColor(requireContext(),R.color.textPrimaryColor))
                        binding.edtGender.setText("", false)
                    }

                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserNamePhoto(newName: String) {
        auth.currentUser?.let { user ->
            val builder = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)

            selectImageUri?.let {
                builder.setPhotoUri(it)
            }

            val profileUpdates = builder.build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveUpdatedProfileData(nickname: String, dob: String, gender: String) {
        val userId = auth.currentUser?.uid ?: return
        val updatedData = mapOf(
            "nickname" to nickname,
            "dateOfBirth" to dob,
            "gender" to gender
        )

        firestore.collection("users").document(userId)
            .set(updatedData, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                loadUserProfileData()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update Firestore data", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

