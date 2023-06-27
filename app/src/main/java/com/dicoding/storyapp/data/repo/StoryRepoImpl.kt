package com.dicoding.storyapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.di.LoginSession
import com.dicoding.storyapp.data.models.StoryDatabase
import com.dicoding.storyapp.data.models.StoryRemoteMediator
import com.dicoding.storyapp.data.models.StoryRoomDataModel
import com.dicoding.storyapp.domain.usecases.StoryRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRepositoryImpl @Inject constructor(
    private val database: StoryDatabase,
    private val api: ApiService,
    private val session: LoginSession
) : StoryRepo {
    override fun getAllStory(): LiveData<PagingData<StoryRoomDataModel>> = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 40, enablePlaceholders = true),
        remoteMediator = StoryRemoteMediator(database, api, session),
        pagingSourceFactory = {
            database.storyDao().getAllStories()
        }
    ).liveData

    override fun getAllStoryWithLocation(): LiveData<List<StoryRoomDataModel>> = liveData {
        val token = "Bearer ${
            session.loginSessionFlow.first {
                it.isNotEmpty()
            }
        }"
        emit(api.getStoriesWithLocation(token).stories)
    }
}