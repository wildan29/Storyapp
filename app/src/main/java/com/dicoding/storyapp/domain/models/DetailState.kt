package com.dicoding.storyapp.domain.models

import com.dicoding.storyapp.data.models.Story

sealed class DetailState {
    object Loading : DetailState()
    data class Success(val story: Story) : DetailState()
    data class Error(val message: String) : DetailState()
}