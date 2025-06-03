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
            val isOnBoardingFinished = onBoardingFinished()
            val isSignedUp = isSignUp()

            if (!isOnBoardingFinished) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
            } else {
                if (!isSignedUp) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_singUpFragment)
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                }
            }
        }, 2000)
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isFinished", false)
    }

    private fun isSignUp(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isSignedUp", false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
