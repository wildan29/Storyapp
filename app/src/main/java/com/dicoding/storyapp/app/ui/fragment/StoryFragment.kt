package com.dicoding.storyapp.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.di.StoryViewModel
import com.dicoding.storyapp.app.ui.adapter.StoryListAdapter
import com.dicoding.storyapp.app.utils.Status
import com.dicoding.storyapp.app.utils.Utils
import com.dicoding.storyapp.databinding.FragmentStoryBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModelStory by viewModels<StoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView = activity?.findViewById<NavigationView>(R.id.nav_view)

        navView?.setCheckedItem(R.id.mains)

        // create adapater
        val adapter = StoryListAdapter(onClick = { id ->
            navigate(id!!)
        })

        val layoutManager = LinearLayoutManager(requireContext())

        binding.apply {
            rvListStory.also {
                it.layoutManager = layoutManager
                it.adapter = adapter
            }

            viewModelStory.getStories()

            viewModelStory.res.observe(viewLifecycleOwner) {
                Utils.showLoading(progressIndicator, it.status == Status.LOADING)
                when (it.status) {
                    Status.SUCCESS -> {
                        adapter.submitList(it.data?.listStory)
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {}
                }
            }
        }


    }

    private fun navigate(id : String){
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}