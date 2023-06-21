package com.dicoding.storyapp.app.ui.activity

import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.app.di.ResponseLoginViewModel
import com.dicoding.storyapp.databinding.ActivityDashboardBinding
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModelLogin by viewModels<ResponseLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // create animation with material shared axis
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val enter = MaterialSharedAxis(MaterialSharedAxis.X, true)
        window.enterTransition = enter

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}