package com.dicoding.storyapp.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.di.HomeViewModel
import com.dicoding.storyapp.app.ui.activity.MainActivity
import com.dicoding.storyapp.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModelHome by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // define binding
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // topAppBar
            topAppBar.setNavigationOnClickListener {
                drawerLayout.open()
            }

            // img profile
            imgProfile.setOnClickListener {
                drawerLayout.open()
            }

            if (savedInstanceState == null) {
                navView.setCheckedItem(R.id.mains)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(
                        R.id.content,
                        StoryFragment(),
                        resources.getString(R.string.story_app_tag)
                    )?.commit()
            }

            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mains -> {
                        activity?.supportFragmentManager?.popBackStackImmediate()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(
                                R.id.content,
                                StoryFragment(),
                                resources.getString(R.string.story_app_tag)
                            )
                            ?.commit()
                        popEveryFragment()
                    }
                    R.id.info -> {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(
                                R.id.content,
                                InfoFragment(),
                                resources.getString(R.string.info_app_tag)
                            )
                            ?.addToBackStack(null)
                            ?.commit()
                        Toast.makeText(
                            requireActivity(),
                            resources.getString(R.string.cooming_soon),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    R.id.logout -> {
                        alertDialog()
                    }
                }
                menuItem.isChecked = true
                drawerLayout.close()
                true
            }

            // on back press
            requireActivity()
                .onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (drawerLayout.isOpen) {
                            drawerLayout.close()
                        } else {
                            // if you want onBackPressed() to be called as normal afterwards
                            if (isEnabled) {
                                isEnabled = false
                                requireActivity().onBackPressed()
                            }
                        }
                    }
                }
                )
        }

    }

    private fun alertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                resources.getString(
                    R.string.logout_title
                )
            )
            .setMessage(resources.getString(R.string.logout_msg))
            .setNegativeButton(resources.getString(R.string.NO)) { _, _ ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.YES)) { _, _ ->
                // Respond to positive button press
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.logout_sucess),
                    Toast.LENGTH_SHORT
                ).show()
                viewModelHome.logout()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                activity?.finish()
            }
            .show()
    }

    private fun popEveryFragment() {
        // Clear all back stack.
        val backStackCount = activity?.supportFragmentManager?.backStackEntryCount
        for (i in 0 until backStackCount!!) {
            // Get the back stack fragment id.
            val backStackId = activity?.supportFragmentManager?.getBackStackEntryAt(i)?.id
            Timber.d("$backStackId")
            if (backStackId != null) {
                activity?.supportFragmentManager?.popBackStack(
                    backStackId,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}