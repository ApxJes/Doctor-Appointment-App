package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

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
        map?.uiSettings?.isZoomControlsEnabled = true
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(yangonLatLong, 12f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
