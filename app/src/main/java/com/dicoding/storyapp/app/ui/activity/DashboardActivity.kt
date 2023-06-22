package com.dicoding.storyapp.app.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDashboardBinding
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navControllerDashboard: NavController
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // create animation with material shared axis
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        val enter = MaterialSharedAxis(MaterialSharedAxis.Y, true)

        window.enterTransition = enter

        window.allowEnterTransitionOverlap = true

        super.onCreate(savedInstanceState)

        // define binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        // create nav host
        val navHostFragmentDashboard =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // create nav controller
        navControllerDashboard = navHostFragmentDashboard.navController

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navControllerDashboard.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {

        val fileListFragment =
            supportFragmentManager.findFragmentByTag(resources.getString(R.string.story_app_tag))

        if (fileListFragment != null) {
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
            if (fileListFragment.isVisible && !(drawerLayout.isOpen)) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }

                this.doubleBackToExitPressedOnce = true

                Toast.makeText(
                    this,
                    resources.getString(R.string.back_button_reminder),
                    Toast.LENGTH_SHORT
                ).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            } else {
                if (drawerLayout.isOpen) {
                    drawerLayout.close()
                } else {
                    super.onBackPressed()
                }
            }
        }

    }
}