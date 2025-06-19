package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

@AndroidEntryPoint
class AccountSetUpFragment : Fragment() {
    private var _bindng: FragmentAccountSetUpBinding? = null
    private val binding get() = _bindng!!

    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var firestore: FirebaseFirestore

    private var currentUserProfileUrl: String? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadProfileImage(it) { imageUrl ->
                    currentUserProfileUrl = imageUrl
                    Glide.with(this).load(imageUrl).into(binding.imgProfile)
                }
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

        val currentUser = auth.currentUser

        currentUser?.let {
            binding.edtName.setText(it.displayName)
            binding.txvEmail.text = it.email
        } ?: run {
            binding.edtName.setText("Unknown")
            binding.txvEmail.text = "abc@gmail.com"
        }

        loadUserProfile()
        binding.imvUploadProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

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

            saveUserProfileData(nickname = nickname, dob = dob, gender = gender, profileImageUrl = currentUserProfileUrl ?: "")
        }

        binding.btnOk.setOnClickListener {
            val action = AccountSetUpFragmentDirections.actionAccountSetUpFragment2ToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateUserName(newName: String) {
        val userName = auth.currentUser
        userName?.let {
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

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

    private fun saveUserProfileData(nickname: String, profileImageUrl: String, dob: String, gender: String) {
        val userId = auth.currentUser?.uid ?: return

        val userData = hashMapOf(
            "nickname" to nickname,
            "profile" to profileImageUrl,
            "dateOfBirth" to dob,
            "gender" to gender
        )

        firestore.collection("users")
            .document(userId)
            .set(userData)
            .addOnSuccessListener {
                binding.flLayout.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if(document != null && document.exists()) {
                    binding.edtNickName.setText(document.getString("nickname") ?: "")
                    binding.edtBirthDate.setText(document.getString("dateOfBirth") ?: "")
                    binding.edtGender.setText(document.getString("gender") ?: "")
                    val profileImageUrl = document.getString("profile") ?: ""

                    if (profileImageUrl.isNotEmpty()) {
                        currentUserProfileUrl = profileImageUrl
                        Glide.with(this).load(profileImageUrl).into(binding.imgProfile)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProfileImage(uri: Uri, onSuccess: (String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        val profileImageRef = FirebaseStorage.getInstance().reference
            .child("profile_images/$userId.jpg")

        profileImageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    Log.e("UPLOAD_ERROR", "Upload failed: ${task.exception?.message}")
                    throw task.exception ?: Exception("Upload Failed")
                }
                profileImageRef.downloadUrl
            }
            .addOnSuccessListener {
                Log.d("UPLOAD_SUCCESS", "Image uploaded successfully: $it")
                onSuccess(it.toString())
            }
            .addOnFailureListener {
                Log.e("UPLOAD_ERROR", "Failed to get download URL: ${it.message}")
                Toast.makeText(requireContext(), "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}