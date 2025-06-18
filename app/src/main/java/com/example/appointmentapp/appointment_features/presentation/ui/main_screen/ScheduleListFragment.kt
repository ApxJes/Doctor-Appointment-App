package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentapp.appointment_features.domain.model.DoctorsVo
import com.example.appointmentapp.appointment_features.presentation.adapter.BookingAdapter
import com.example.appointmentapp.appointment_features.presentation.viewModel.AppointmentViewModel
import com.example.appointmentapp.databinding.FragmentScheduleListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ScheduleListFragment : Fragment() {

    private var _binding: FragmentScheduleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookingAdapter: BookingAdapter
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookingAdapter = BookingAdapter(
            onCancelClick = {
                appointmentViewModel.deleteAppointment(it)
            },
            onRescheduleClick = { appointment ->
                val doctor = DoctorsVo (
                    id = appointment.doctorId,
                    name = appointment.doctorName,
                    specialized = appointment.specialization,
                    hospital = appointment.hospital,
                    picture = appointment.imageUrl,
                    about = null,
                    experience = null,
                    patients = null,
                    rating = null,
                    workTime = null
                )

                val action = AppointmentFragmentDirections
                    .actionAppointmentFragmentToBookAppointmentFragment(doctor)
                findNavController().navigate(action)
            }
        )
        setUpBookingRecyclerView()
        fetchBookingList()

    }

    private fun fetchBookingList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.allAppointments.collectLatest {  appointments ->
                    bookingAdapter.submitList(appointments)
                }
            }
        }
    }

    private fun setUpBookingRecyclerView() {
        binding.rcvBooking.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}