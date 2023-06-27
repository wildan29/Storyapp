package com.dicoding.storyapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_remote_key")
data class StoryRemoteKey(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)