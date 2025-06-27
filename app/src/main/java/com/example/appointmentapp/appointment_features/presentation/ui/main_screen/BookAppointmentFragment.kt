package com.example.appointmentapp.appointment_features.presentation.ui.main_screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.appointmentapp.R
import com.example.appointmentapp.appointment_features.data.local.AppointmentEntity
import com.example.appointmentapp.appointment_features.presentation.viewModel.LocallySaveAppointmentViewModel
import com.example.appointmentapp.databinding.FragmentBookAppointmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class BookAppointmentFragment : Fragment() {

    private var _binding: FragmentBookAppointmentBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private var selectedButton: AppCompatButton? = null
    private val args: BookAppointmentFragmentArgs by navArgs()
    private val appointmentViewModel: LocallySaveAppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackPress.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnConfirm.setOnClickListener {
            when {
                selectedDate.isNullOrEmpty() && selectedTime.isNullOrEmpty() -> {
                    Snackbar.make(requireView(), "Please select a date and time", Snackbar.LENGTH_SHORT).show()
                }
                selectedDate.isNullOrEmpty() -> {
                    Snackbar.make(requireView(), "Please select a date", Snackbar.LENGTH_SHORT).show()
                }
                selectedTime.isNullOrEmpty() -> {
                    Snackbar.make(requireView(), "Please select a time", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    val doctorName = args.doctor.name
                    binding.txvSelectedDateResult.text = "Your Appointment with $doctorName is confirm for $selectedDate at $selectedTime"
                    binding.cardSelectedDate.visibility = View.VISIBLE

                    val appointment = AppointmentEntity (
                        doctorId = args.doctor.id ?: "0",
                        doctorName = args.doctor.name ?: "Unknown",
                        specialization = args.doctor.specialized ?: "Unknown",
                        hospital = args.doctor.hospital ?: "Unknown",
                        imageUrl = args.doctor.picture ?: "Unknown",
                        selectedDate = selectedDate!!,
                        selectedTime = selectedTime!!
                    )

                    appointmentViewModel.booking(appointment)
                }
            }
        }

        binding.btnDone.setOnClickListener {
            binding.cardSelectedDate.visibility = View.GONE
        }

        val timeButtons = listOf(
            binding.btn9Am,
            binding.btn930Am,
            binding.btn10Am,
            binding.btn1030Am,
            binding.btn11Am,
            binding.btn1130Am,
            binding.btn3Pm,
            binding.btn330Pm,
            binding.btn4Pm,
            binding.btn430Pm,
            binding.btn5Pm,
            binding.btn530Pm
        )

        timeButtons.forEach { button ->
            button.setOnClickListener {
                handleTimeButtonClick(button)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectCalendar = Calendar.getInstance()
                selectCalendar.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectCalendar.time)

                binding.cardSelectedDate.visibility = View.GONE
            },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun handleTimeButtonClick(button: AppCompatButton) {
        selectedButton?.setBackgroundResource(R.drawable.auth_bg)
        selectedButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))

        selectedButton = button
        selectedTime = button.text.toString()

        selectedButton?.setBackgroundResource(R.drawable.category_bg)
        selectedButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        binding.cardSelectedDate.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
