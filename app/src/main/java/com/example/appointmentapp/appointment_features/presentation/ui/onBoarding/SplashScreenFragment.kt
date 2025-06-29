package com.example.appointmentapp.appointment_features.presentation.ui.onBoarding

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appointmentapp.R
import com.example.appointmentapp.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logo.apply {
            alpha = 0f
            scaleX = 0.8f
            scaleY = 0.8f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val isOnBoardingFinished = OnBoardingPreference.isOnboardingFinished(requireContext())

            if (!isOnBoardingFinished) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
            } else {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
            }
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
