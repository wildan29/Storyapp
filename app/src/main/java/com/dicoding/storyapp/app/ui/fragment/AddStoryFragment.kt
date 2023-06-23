package com.dicoding.storyapp.app.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.di.AddStoryViewModel
import com.dicoding.storyapp.app.utils.Utils.getFileFromUri
import com.dicoding.storyapp.app.utils.Utils.reduceFileImage
import com.dicoding.storyapp.databinding.FragmentAddStoryBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var imageFile: File? = null
    private val viewModelAddStory by viewModels<AddStoryViewModel>()
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = getFileFromUri(uri, requireContext())
                imageFile = myFile
                if (imageFile != null) {
                    Glide.with(requireContext())
                        .load(imageFile)
                        .into(binding.image)
                }
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // give permission
        if (!allPermissions()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.apply {
            btnCameraX.setOnClickListener {
                findNavController().navigate(R.id.action_addStoryFragment_to_cameraFragment)
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnUpload.setOnClickListener {
                uploadImage()
                val addStoryState = viewModelAddStory.addStoryState
                lifecycleScope.launch {
                    addStoryState.collect() {
                        if (it.isNavigate == true) {
                            findNavController().navigate(R.id.action_addStoryFragment_to_homeFragment)
                            Toast.makeText(
                                requireContext(),
                                "Story upload was successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModelAddStory.navigates()
                        }
                        it.error?.let { error ->
                            Snackbar.make(
                                requireView(),
                                error,
                                Snackbar.LENGTH_SHORT
                            ).addCallback(
                                object : Snackbar.Callback() {
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        super.onDismissed(transientBottomBar, event)
                                        viewModelAddStory.showErrors()
                                    }
                                }
                            ).show()
                        }
                    }
                }
            }

            setFragmentResultListener("requestImage") { _, bundle ->
                bundle.getString("responseImage")?.let { imageFile = File(it) }
                if (imageFile != null) {
                    Glide.with(requireContext())
                        .load(imageFile)
                        .into(binding.image)
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissions()) {
                Toast.makeText(
                    activity,
                    "Did not get permissions!",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun uploadImage() {
        val description = binding.edtDesc.text.toString()

        if (imageFile != null && description.isNotEmpty()) {
            val file = reduceFileImage(imageFile as File)
            viewModelAddStory.addStories(file, description)
        } else {
            when {
                (description.isEmpty()) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in the description first!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                (imageFile == null) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please insert an image file first!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun allPermissions() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}