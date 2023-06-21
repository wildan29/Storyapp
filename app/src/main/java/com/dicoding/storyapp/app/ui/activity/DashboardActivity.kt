package com.dicoding.storyapp.app.ui.activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.databinding.ActivityDashboardBinding
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // create animation with material shared axis
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val enter = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        window.enterTransition = enter
        window.allowEnterTransitionOverlap = true

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}