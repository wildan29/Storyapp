package com.dicoding.storyapp.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.di.MapStoryViewModel
import com.dicoding.storyapp.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModelMapStory by viewModels<MapStoryViewModel>()
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.story_map_fragment) as SupportMapFragment

        viewModelMapStory.getAllStory.observe(viewLifecycleOwner) { storiesWithLocation ->
            val callback = OnMapReadyCallback { googleMap ->
                googleMap.uiSettings.apply {
                    isZoomControlsEnabled = true
                    isIndoorLevelPickerEnabled = true
                    isCompassEnabled = true
                    isMapToolbarEnabled = true
                }

                storiesWithLocation.forEach { story ->
                    val latLng = LatLng(story.lat, story.lon)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(story.name))
                    boundsBuilder.include(latLng)
                }

                val bounds: LatLngBounds = boundsBuilder.build()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        100
                    )
                )
            }

            mapFragment.getMapAsync(callback)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}