package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocationViewModel
import com.example.appointmentapp.databinding.FragmentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var activeMarker: Marker? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
        val yangonLatLng = LatLng(16.8409, 96.1735)

        addHospitalMarker(
            LatLng(16.7791, 96.1490),
            "General Hospital Yangon",
            getString(R.string.general_hospital_yangon),
            R.drawable.general_hospital_yangon
        )

        addHospitalMarker(
            LatLng(16.7875, 96.1349),
            "Children's Hospital Yangon",
            getString(R.string.children_hospital_yanagon),
            R.drawable.children_hospital_yangon
        )

        addHospitalMarker(
            LatLng(21.9774, 96.0904),
            "General Hospital Mandalay",
            getString(R.string.general_hospital_manadalay),
            R.drawable.general_hospital_manadalay
        )

        map?.setInfoWindowAdapter(CustomWindowInfoAdapter(requireContext()))

        map?.setOnMarkerClickListener {
            activeMarker = it
            it.showInfoWindow()
            true
        }

        map?.setOnMapClickListener {
            activeMarker?.hideInfoWindow()
        }

        map?.setOnCameraMoveListener {
            activeMarker?.hideInfoWindow()
        }

        map?.uiSettings?.isZoomControlsEnabled = true
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(yangonLatLng, 12f))
    }

    private fun addHospitalMarker(
        latLng: LatLng,
        title: String,
        description: String,
        imageRes: Int
    ) {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Location")
            .snippet(title)
            .icon(BitmapDescriptorFactory.fromBitmap(getBitMapFromDrawable(R.drawable.hospital_icon)))

        val marker = map?.addMarker(markerOptions)
        marker?.tag = WindowInfoData(title, description, imageRes)
    }

    private fun getBitMapFromDrawable(resId: Int): Bitmap? {
        val drawable = ResourcesCompat.getDrawable(resources, resId, null)
        return drawable?.let {
            val bitmap = createBitmap(120, 120)
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            bitmap
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
