package com.dicoding.storyapp.app.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.di.LoginSession
import com.dicoding.storyapp.data.models.AddStoryResponseDataModel
import com.dicoding.storyapp.data.models.RegisterResponseDataModel
import com.dicoding.storyapp.data.repo.MainRepo
import com.dicoding.storyapp.domain.models.AddStoryState
import com.dicoding.storyapp.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel

class AddStoryViewModel @Inject constructor(
    private val mainRepo: MainRepo,
    private val loginSession: LoginSession
) : ViewModel() {
    private val _addStoryState = MutableStateFlow(AddStoryState())
    val addStoryState: StateFlow<AddStoryState>
        get() = _addStoryState

    private val _res = MutableLiveData<Resource<AddStoryResponseDataModel>>()
    val res: LiveData<Resource<AddStoryResponseDataModel>>
        get() = _res

    fun addStories(photo: File, desc: String) = viewModelScope.launch {
        val getDesc = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )

        val bearerToken = "Bearer ${
            loginSession.loginSessionFlow.first {
                it.isNotEmpty()
            }
        }"

        _res.postValue(Resource.loading(null))
        mainRepo.addStories(imageMultipart, getDesc, bearerToken).let {
            if (it.isSuccessful) {
                navigate()
                _res.postValue(Resource.success(it.body()))
            } else {
                val jsonObj =
                    JSONObject(it.errorBody()!!.charStream().readText()).getString("message")
                showError(jsonObj)
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

    fun navigate() {
        _addStoryState.update { change ->
            change.copy(isNavigate = true)
        }
    }

    fun navigates() {
        _addStoryState.update { change ->
            change.copy(isNavigate = null)
        }
    }

    fun showError(message: String) {
        _addStoryState.update { error ->
            error.copy(error = "Register Failed, $message!")
        }
    }

    fun showErrors() {
        _addStoryState.update { error ->
            error.copy(error = null)
        }
    }
}