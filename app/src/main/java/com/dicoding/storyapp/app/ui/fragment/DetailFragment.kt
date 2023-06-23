package com.dicoding.storyapp.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dicoding.storyapp.app.di.DetailViewModel
import com.dicoding.storyapp.data.models.ListStoryItem
import com.dicoding.storyapp.databinding.FragmentDetailBinding
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
                activity?.onBackPressed()
            }

            val story: ListStoryItem? = arguments?.getParcelable("story")

            if (story != null) {
                displayDetail(story)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun displayDetail(data: ListStoryItem) {
        binding.apply {
            Glide.with(requireContext())
                .load(data.photoUrl)
                .into(detailStory)
            title.text = data.name
            sub.text = data.description
        }
    }
}