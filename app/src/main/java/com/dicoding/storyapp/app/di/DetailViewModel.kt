package com.dicoding.storyapp.app.di

import androidx.lifecycle.*
import com.dicoding.storyapp.data.di.LoginSession
import com.dicoding.storyapp.data.repo.MainRepo
import com.dicoding.storyapp.domain.models.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mainRepo: MainRepo,
    private val loginSession: LoginSession
) : ViewModel() {
    private val id: String? = savedStateHandle["id"]

    private val _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState>
        get() = _detailState

    init {
        if (id != null) {
            getDetailStory(id)
        }
    }

    private fun getDetailStory(id: String) = viewModelScope.launch {
        _detailState.postValue(DetailState.Loading)
        val bearerToken = "Bearer ${
            loginSession.loginSessionFlow.first {
                it.isNotEmpty()
            }
        }"
        mainRepo.getStoriesDetail(id, bearerToken).let {
            if (it.isSuccessful) {
                _detailState.postValue(DetailState.Success(it.body()?.story!!))
            } else {
                val jsonObj =
                    JSONObject(it.errorBody()!!.charStream().readText()).getString("message")
                _detailState.postValue(DetailState.Error(jsonObj))
            }
        }
    }
}