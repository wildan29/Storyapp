package com.dicoding.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class AllStoriesResponseRoom(
    val error: Boolean,
    val message: String,
    @SerializedName("listStory")
    val stories: List<StoryRoomDataModel>
)