package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocationViewModel
import com.example.appointmentapp.appointment_features.presentation.viewModel.NearByHospitalViewModel
import com.example.appointmentapp.databinding.FragmentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val locationViewModel: LocationViewModel by activityViewModels()
    private val getNearByHospitalViewModel: NearByHospitalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.rgMapTypes.setOnCheckedChangeListener { _, typeId ->
            when (typeId) {
                R.id.rbNormal -> map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                R.id.rbHybrid -> map?.mapType = GoogleMap.MAP_TYPE_HYBRID
                R.id.rbSatellite -> map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        checkLocationPermissionAndShow()
    }

    private fun checkLocationPermissionAndShow() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
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
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))

                    getNearByHospitalViewModel.fetchHospitals(it.latitude, it.longitude)
                    fetchNearByHospitals()

                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)

                    if (!address.isNullOrEmpty()) {
                        val city = address[0].locality ?: "Unknown city"
                        val country = address[0].countryName ?: "Unknown Country"
                        locationViewModel.updateLocation(city, country)
                    }
                }
            }
        }
    }

    private fun fetchNearByHospitals() {
        lifecycleScope.launchWhenStarted {
            getNearByHospitalViewModel.hospital.collectLatest { hospitalList ->
                hospitalList.onEach { hospital ->
                    val lat = hospital.lat?.toDoubleOrNull()
                    val lon = hospital.lon?.toDoubleOrNull()

                    if((lat != null && lon != null)) {
                        map?.addMarker(
                            MarkerOptions()
                                .position(LatLng(lat, lon))
                                .title(hospital.displayName)
                                .icon(BitmapDescriptorFactory.fromBitmap(getDrawableToBitmap(R.drawable.hospital_icon)))
                        )
                    }
                }
            }
        }
    }

    private fun getDrawableToBitmap(resId: Int): Bitmap? {
        val drawable = ResourcesCompat.getDrawable(resources, resId, null) ?: return null

        val bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}