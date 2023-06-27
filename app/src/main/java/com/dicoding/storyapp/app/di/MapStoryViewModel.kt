package com.dicoding.storyapp.app.di

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.domain.usecases.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapStoryViewModel @Inject constructor(
    storyRepo: StoryRepo
) : ViewModel() {
    val getAllStory = storyRepo.getAllStoryWithLocation()
}