package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocationViewModel
import com.example.appointmentapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationViewModel.locationText.observe(viewLifecycleOwner) { location ->
            binding.txvLocation.text = location
        }

        checkLocationPermissionAndFetch()

        binding.imvDoctorImageBanner.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDoctorListFragment())
        }
    }

    private fun checkLocationPermissionAndFetch() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchUserLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            fetchUserLocation()
        } else {
            locationViewModel.updateLocation("Permission denied", "")
            Snackbar.make(
                binding.root,
                "We couldn’t access your location. Please allow location permission to get nearby suggestions.",
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    private fun fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)

                    if (!address.isNullOrEmpty()) {
                        val city = address[0].locality ?: "Unknown city"
                        val country = address[0].countryName ?: "Unknown Country"
                        locationViewModel.updateLocation(city, country)
                    } else {
                        locationViewModel.updateLocation("Unknown city", "Unknown Country")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
