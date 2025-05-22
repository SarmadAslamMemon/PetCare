package com.example.petcare.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.petcare.R
import android.os.Handler
import android.os.Looper
import android.util.Log

class MainDashboardFragment : Fragment(R.layout.fragment_dash_board) {
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Get references to the cards
            val healthTipsCard = view.findViewById<View>(R.id.healthTipCardView)
            val petCareCard = view.findViewById<View>(R.id.petCareCardView)
            val consultationCard = view.findViewById<View>(R.id.consultationCardView)

            // Set initial visibility
            healthTipsCard?.visibility = View.INVISIBLE
            petCareCard?.visibility = View.INVISIBLE
            consultationCard?.visibility = View.INVISIBLE

            // Load animations
            val slideInAnimation = try {
                AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
            } catch (e: Exception) {
                Log.e("MainDashboard", "Failed to load animation", e)
                null
            }

            // Animate cards with delays
            handler?.apply {
                postDelayed({
                    healthTipsCard?.let {
                        it.visibility = View.VISIBLE
                        slideInAnimation?.let { anim -> it.startAnimation(anim) }
                    }
                }, 100)

                postDelayed({
                    petCareCard?.let {
                        it.visibility = View.VISIBLE
                        slideInAnimation?.let { anim -> it.startAnimation(anim) }
                    }
                }, 200)

                postDelayed({
                    consultationCard?.let {
                        it.visibility = View.VISIBLE
                        slideInAnimation?.let { anim -> it.startAnimation(anim) }
                    }
                }, 300)
            }
        } catch (e: Exception) {
            Log.e("MainDashboard", "Error in onViewCreated", e)
        }
    }

    override fun onDestroyView() {
        // Remove any pending animations
        handler?.removeCallbacksAndMessages(null)
        handler = null
        super.onDestroyView()
    }
} 