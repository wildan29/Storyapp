package com.dicoding.storyapp.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.storyapp.app.di.DetailViewModel
import com.dicoding.storyapp.app.utils.Utils
import com.dicoding.storyapp.databinding.FragmentDetailBinding
import com.dicoding.storyapp.domain.models.DetailState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModelDetail by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            topAppBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            
            viewModelDetail.detailState.observe(viewLifecycleOwner) {
                Utils.showLoading(progressIndicator, it is DetailState.Loading)
                when (it) {
                    is DetailState.Loading -> {}
                    is DetailState.Success -> {
                        Glide.with(requireContext())
                            .load(it.story.photoUrl)
                            .into(detailStory)
                        title.text = it.story.name
                        sub.text = it.story.description
                    }
                    is DetailState.Error -> {
                        Snackbar.make(
                            requireView(),
                            it.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}