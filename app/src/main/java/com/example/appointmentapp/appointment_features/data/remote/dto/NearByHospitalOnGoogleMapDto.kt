package com.example.appointmentapp.appointment_features.data.remote.dto


import com.example.appointmentapp.appointment_features.domain.model.GetNearByHospitalOnGoogleMapVo
import com.google.gson.annotations.SerializedName

data class NearByHospitalOnGoogleMapDto(
    @SerializedName("addresstype")
    val addresstype: String?,
    @SerializedName("boundingbox")
    val boundingbox: List<String?>?,
    @SerializedName("class")
    val classX: String?,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("importance")
    val importance: Double?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("licence")
    val licence: String?,
    @SerializedName("lon")
    val lon: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("osm_id")
    val osmId: Long?,
    @SerializedName("osm_type")
    val osmType: String?,
    @SerializedName("place_id")
    val placeId: Int?,
    @SerializedName("place_rank")
    val placeRank: Int?,
    @SerializedName("type")
    val type: String?
) {
    fun toHospitalVo(): GetNearByHospitalOnGoogleMapVo {
        return GetNearByHospitalOnGoogleMapVo(
            displayName = displayName ?: "Unknown",
            lat = lat ?: "0.0",
            lon = lon ?: "0.0"
        )
    }
}