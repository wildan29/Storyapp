package com.dicoding.storyapp.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dicoding.storyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // splash screen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // define binding
        binding = ActivityMainBinding.inflate(layoutInflater)

        // splash screen animation
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            splashScreenViewProvider.iconView
                .animate()
                .setDuration(
                    500
                ).translationY(-0f)
                .alpha(0f)
                .withEndAction {
                    // After the fade out, remove the
                    // splash and set content view
                    splashScreenViewProvider.remove()
                }.start()
        }

        // Light mode only
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(binding.root)
    }
}