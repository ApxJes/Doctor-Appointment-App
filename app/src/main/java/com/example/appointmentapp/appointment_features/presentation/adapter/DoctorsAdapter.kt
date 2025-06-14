package com.example.appointmentapp.appointment_features.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.databinding.DoctorsLayoutBinding

class DoctorsAdapter: RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<DoctorsVo>() {
        override fun areItemsTheSame(
            oldItem: DoctorsVo,
            newItem: DoctorsVo
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DoctorsVo,
            newItem: DoctorsVo
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorsViewHolder {
        val binding = DoctorsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DoctorsViewHolder,
        position: Int
    ) {
        val doctors = differ.currentList[position]
        with(holder.binding) {
            Glide.with(root.context)
                .load(doctors.picture)
                .into(imvDoctor)

            txvDoctorName.text = doctors.name
            txvSpecialized.text = doctors.specialized
            txvRating.text = doctors.rating
            txvHospitalAddress.text = doctors.hospital

            root.setOnClickListener {
                onClick?.invoke(doctors)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onClick: ((DoctorsVo) -> Unit)? = null
    fun setOnClickListener(listener: (DoctorsVo) -> Unit) {
        onClick = listener
    }

    inner class DoctorsViewHolder(val binding: DoctorsLayoutBinding): RecyclerView.ViewHolder(binding.root)
}