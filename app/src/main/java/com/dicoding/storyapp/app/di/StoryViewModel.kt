package com.dicoding.storyapp.app.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.di.LoginSession
import com.dicoding.storyapp.data.models.AllStoriesResponse
import com.dicoding.storyapp.data.models.StoryRoomDataModel
import com.dicoding.storyapp.data.repo.MainRepo
import com.dicoding.storyapp.domain.models.Resource
import com.dicoding.storyapp.domain.usecases.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val mainRepo: MainRepo? = null,
    private val loginSession: LoginSession? = null,
    private val repository: StoryRepo
) : ViewModel() {

    private val _res = MutableLiveData<Resource<AllStoriesResponse>>()
    val res: LiveData<Resource<AllStoriesResponse>>
        get() = _res

    val allStory: LiveData<PagingData<StoryRoomDataModel>> = repository.getAllStory()
        .cachedIn(viewModelScope)

    fun getStories() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        val bearerToken = "Bearer ${
            loginSession?.loginSessionFlow?.first {
                it.isNotEmpty()
            }
        }"
        mainRepo!!.getStories(bearerToken).let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

}