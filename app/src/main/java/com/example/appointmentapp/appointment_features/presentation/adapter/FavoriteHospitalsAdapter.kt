package com.example.appointmentapp.appointment_features.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.domain.model.HospitalsVo
import com.example.appointmentapp.databinding.HospitalLayoutBinding
import com.example.appointmentapp.databinding.SaveHospitalsBinding


class FavoriteHospitalsAdapter(
    private val onSaveClick: (HospitalsVo) -> Unit,
    private val isHospitalSaved: (HospitalsVo) -> Boolean
): RecyclerView.Adapter<FavoriteHospitalsAdapter.FavoriteHospitalsViewHolder>(){
    private val differCallBack = object: DiffUtil.ItemCallback<HospitalsVo>(){
        override fun areItemsTheSame(
            oldItem: HospitalsVo,
            newItem: HospitalsVo
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HospitalsVo,
            newItem: HospitalsVo
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteHospitalsViewHolder {
        val binding = SaveHospitalsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteHospitalsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteHospitalsViewHolder,
        position: Int
    ) {
        val hospital = differ.currentList[position]
        with(holder.binding) {
            Glide.with(root.context)
                .load(hospital.image)
                .into(imvHospital)

            txvHospitalName.text = hospital.name
            txvHospitalLocation.text = hospital.location
            txvRating.text = hospital.rating

            val isSaved = isHospitalSaved(hospital)
            btnSave.setImageResource(
                if (isSaved) R.drawable.saved else R.drawable.to_save
            )

            btnSave.setOnClickListener {
                Log.d("HospitalsAdapter", "Save clicked for: ${hospital.name}")
                onSaveClick(hospital)

                val currentPosition = holder.adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(currentPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class FavoriteHospitalsViewHolder(val binding: SaveHospitalsBinding): RecyclerView.ViewHolder(binding.root)
}