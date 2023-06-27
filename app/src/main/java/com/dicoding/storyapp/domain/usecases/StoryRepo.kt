package com.dicoding.storyapp.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.data.models.StoryRoomDataModel
import java.io.File

interface StoryRepo {
    fun getAllStory(): LiveData<PagingData<StoryRoomDataModel>>

    fun getAllStoryWithLocation(): LiveData<List<StoryRoomDataModel>>
}