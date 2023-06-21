package com.dicoding.storyapp.app.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.data.models.RegisterResponseDataModel
import com.dicoding.storyapp.data.repo.MainRepo
import com.dicoding.storyapp.domain.models.RegisterState
import com.dicoding.storyapp.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ResponseRegisterViewModel @Inject constructor(private val mainRepo: MainRepo) : ViewModel() {
    private val _res = MutableLiveData<Resource<RegisterResponseDataModel>>()
    val res: LiveData<Resource<RegisterResponseDataModel>>
        get() = _res

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState>
        get() = _state

    fun responseRegister(registerDataModel: RegisterDataModel) = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        mainRepo.responseRegister(registerDataModel).let {
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
        _state.update { change ->
            change.copy(isNavigate = true)
        }
    }

    fun navigates() {
        _state.update { change ->
            change.copy(isNavigate = null)
        }
    }

    fun showError(message: String) {
        _state.update { error ->
            error.copy(error = "Register Failed, $message!")
        }
    }

    fun showErrors() {
        _state.update { error ->
            error.copy(error = null)
        }
    }
}