package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.appointment_features.presentation.adapter.HospitalsAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.HospitalsViewModel
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocallySaveHospitalsViewModel
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocationViewModel
import com.example.appointmentapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var hospitalAdapter: HospitalsAdapter
    private val hospitalsViewModel: HospitalsViewModel by viewModels()
    private val locallySaveHospitalsViewModel: LocallySaveHospitalsViewModel by viewModels()

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hospitalAdapter = HospitalsAdapter(
            onSaveClick = { hospital ->
                locallySaveHospitalsViewModel.toggleHospitalSave(hospital)
            },
            isHospitalSaved = { hospital ->
               locallySaveHospitalsViewModel.isHospitalSaved(hospital)
            }
        )

        lifecycleScope.launchWhenStarted {
            locallySaveHospitalsViewModel.state.collectLatest {
                hospitalAdapter.notifyDataSetChanged()
            }
        }


        initLocationClient()
        observeLocation()
        setupClickListeners()
        checkLocationPermissionAndFetch()

        setUpHospitalViewModel()
        setUpHospitalsListObserver()
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
            if(isLocationEnable()) {
                fetchUserLocation()
            } else {
                locationViewModel.updateLocation("Unknown city", "Unknown Country")
                promptEnableLocation()
            }
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
            if(isLocationEnable()) {
                fetchUserLocation()
            } else {
                locationViewModel.updateLocation("Unknown city", "Unknown Country")
                promptEnableLocation()
            }
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

    private fun isLocationEnable(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun promptEnableLocation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable Location")
            .setMessage("Location services are required for this feature. Please enable location.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setUpHospitalsListObserver() {
        lifecycleScope.launchWhenStarted {
            hospitalsViewModel.getHospitals.collectLatest { hospitalList ->
                hospitalAdapter.differ.submitList(hospitalList)
            }
        }
    }

    private fun setUpHospitalViewModel() {
        binding.rcvHospital.apply {
            adapter = hospitalAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onResume() {
        super.onResume()
        if (isLocationPermissionGranted() && isLocationEnable()) {
            fetchUserLocation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
