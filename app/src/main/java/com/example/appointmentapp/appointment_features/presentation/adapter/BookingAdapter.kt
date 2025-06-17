package com.example.appointmentapp.appointment_features.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.databinding.BookingBinding

class BookingAdapter : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    private val bookingList = mutableListOf<AppointmentEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<AppointmentEntity>) {
        bookingList.clear()
        bookingList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = BookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookingList[position])
    }

    override fun getItemCount(): Int = bookingList.size

    inner class BookingViewHolder(val binding: BookingBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(appointment: AppointmentEntity) = with(binding) {
            txvBookingDateAndTime.text = "${appointment.selectedDate} - ${appointment.selectedTime}"
            txvDoctorName.text = appointment.doctorName
            txvSpecialized.text = appointment.specialization
            txvHospitalAddress.text = appointment.hospital

            Glide.with(imvDoctor.context)
                .load(appointment.imageUrl)
                .placeholder(R.drawable.user_pf)
                .into(imvDoctor)

            btnCancel.setOnClickListener {
            }

            btnReSchedule.setOnClickListener {
            }
        }
    }
}
