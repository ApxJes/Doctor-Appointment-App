package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
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

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocationClient()
        observeLocation()
        setupClickListeners()
        checkLocationPermissionAndFetch()
    }

    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun observeLocation() {
        locationViewModel.locationText.observe(viewLifecycleOwner) { location ->
            binding.txvLocation.text = location
        }
    }

    private fun setupClickListeners() {
        binding.edtSerach.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDoctorListFragment())
        }

        binding.txvSeeAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCategoriesFragment())
        }

        setupCategoryButtons()
    }

    private fun setupCategoryButtons() {
        binding.btnDentistry.setOnClickListener { navigateToCategory("Dentist") }
        binding.btnCardiologist.setOnClickListener { navigateToCategory("Cardiologist") }
        binding.btnPulmonologist.setOnClickListener { navigateToCategory("Pulmonologist") }
        binding.btnGeneral.setOnClickListener { navigateToCategory("General") }
        binding.btnNeurology.setOnClickListener { navigateToCategory("Neurologist") }
        binding.btnGastroenterology.setOnClickListener { navigateToCategory("Gastroenterologist") }
        binding.btnLaboratory.setOnClickListener { navigateToCategory("Laboratory") }
        binding.btnVeccination.setOnClickListener { navigateToCategory("Veccination") }
    }

    private fun navigateToCategory(category: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun checkLocationPermissionAndFetch() {
        if (isLocationPermissionGranted()) {
            fetchUserLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
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

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun fetchUserLocation() {
        if (!isLocationPermissionGranted()) return

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val city = addresses[0].locality ?: "Unknown city"
                    val country = addresses[0].countryName ?: "Unknown Country"
                    locationViewModel.updateLocation(city, country)
                } else {
                    locationViewModel.updateLocation("Unknown city", "Unknown Country")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
