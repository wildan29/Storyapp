package com.dicoding.storyapp.app.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.di.LoginSession
import com.dicoding.storyapp.data.models.AllStoriesResponse
import com.dicoding.storyapp.data.repo.MainRepo
import com.dicoding.storyapp.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val mainRepo: MainRepo,
    private val loginSession: LoginSession
) : ViewModel() {

    private val _res = MutableLiveData<Resource<AllStoriesResponse>>()
    val res: LiveData<Resource<AllStoriesResponse>>
        get() = _res

    fun getStories() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        val bearerToken = "Bearer ${
            loginSession.loginSessionFlow.first {
                it.isNotEmpty()
            }
        }"
        mainRepo.getStories(bearerToken).let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

}