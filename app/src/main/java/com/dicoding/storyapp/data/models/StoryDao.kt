package com.dicoding.storyapp.data.models

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryRoomDataModel>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoryRoomDataModel>

    @Query("DELETE FROM story")
    suspend fun deleteAllStories()

}