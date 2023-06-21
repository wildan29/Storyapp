package com.dicoding.storyapp.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navControllerLogin: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // animation with MaterialSharedAxis
        val exit = MaterialSharedAxis(MaterialSharedAxis.X, true)
        window.exitTransition = exit

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // create nav host
        val navHostFragmentLogin =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment

        // create nav controller
        navControllerLogin = navHostFragmentLogin.navController

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navControllerLogin.navigateUp() || super.onSupportNavigateUp()
    }

}