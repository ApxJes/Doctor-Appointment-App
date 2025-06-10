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
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentLocationBinding
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
            when(typeId) {
                R.id.rbNormal -> {
                    map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                }

                R.id.rbHybrid -> {
                    map?.mapType = GoogleMap.MAP_TYPE_HYBRID
                }

                R.id.rbSatellite -> {
                    map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL

        val yangonLatLong = LatLng(16.8409, 96.1735)

        val generalHospitalLatLong = LatLng(16.7791, 96.1490)
        val generalHospitalMarker = MarkerOptions()
        generalHospitalMarker.position(generalHospitalLatLong)
        generalHospitalMarker.title("Location")
        generalHospitalMarker.snippet("General Hospital Yangon")
        generalHospitalMarker.icon(BitmapDescriptorFactory.fromBitmap(getBitMapFromDrawable(R.drawable.hospital_icon)))
        val marker = map?.addMarker(generalHospitalMarker)
        marker?.tag = WindowInfoData(
            title = "General Hospital Yangon",
            des = getString(R.string.general_hospital_yangon),
            image = R.drawable.general_hospital_yangon
        )

        map?.setInfoWindowAdapter(CustomWindowInfoAdapter(requireContext()))
        map?.setOnMarkerClickListener { marker ->
            activeMarker = marker
            marker.showInfoWindow()
            true
        }

        map?.setOnMapClickListener {
            activeMarker?.hideInfoWindow()
        }

        map?.setOnCameraMoveListener {
            activeMarker?.hideInfoWindow()
        }

        map?.uiSettings?.isZoomControlsEnabled = true
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(yangonLatLong, 12f))
    }

    private fun getBitMapFromDrawable(resId: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val drawable = ResourcesCompat.getDrawable(resources, resId, null)
        drawable?.let {
            bitmap = createBitmap(120, 120)
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
        }

        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
