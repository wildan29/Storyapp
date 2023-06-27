package com.dicoding.storyapp.data.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<StoryRemoteKey>)

    @Query("SELECT * FROM story_remote_key WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): StoryRemoteKey?

    @Query("DELETE FROM story_remote_key")
    suspend fun deleteRemoteKeys()

}