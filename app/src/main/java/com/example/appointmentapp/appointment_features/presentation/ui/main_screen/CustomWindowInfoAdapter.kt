package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.appointmentapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomWindowInfoAdapter(
    private val context: Context
): GoogleMap.InfoWindowAdapter{

    @SuppressLint("InflateParams")
    override fun getInfoWindow(marker: Marker?): View? {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.custom_window_info, null)

        val image: ImageView = view.findViewById(R.id.imvHospital)
        val title: TextView =  view.findViewById(R.id.txvHospitalName)
        val des: TextView =  view.findViewById(R.id.txvHospitalDes)

        val data = marker?.tag as? WindowInfoData ?: return null
        title.text = data.title
        des.text = data.des
        image.setImageResource(data.image)

        return view
    }

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }
}