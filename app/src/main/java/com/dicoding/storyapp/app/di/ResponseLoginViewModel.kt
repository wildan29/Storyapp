package com.dicoding.storyapp.app.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.di.LoginSession
import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.data.models.LoginResponseDataModel
import com.dicoding.storyapp.data.repo.MainRepo
import com.dicoding.storyapp.domain.models.LoginState
import com.dicoding.storyapp.domain.models.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ResponseLoginViewModel @Inject constructor(
    private val mainRepo: MainRepo,
    private val loginSession: LoginSession
) : ViewModel() {
    private val _res = MutableLiveData<Resource<LoginResponseDataModel>>()
    val res: LiveData<Resource<LoginResponseDataModel>>
        get() = _res

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState>
        get() = _state

    fun responseLogin(loginDataModel: LoginDataModel) = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        mainRepo.responseLogin(loginDataModel).let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()))
                it.body()?.loginResult?.also { res ->
                    loginSession.updateLoginSession(res.token!!)
                    loginSession.setUsername(res.name!!)
                    navigate()
                }
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
                val jsonObj =
                    JSONObject(it.errorBody()!!.charStream().readText()).getString("message")
                showError(jsonObj)
            }
        }
    }

    private fun navigate() {
        _state.update { change ->
            change.copy(isNavigate = true)
        }
    }

    fun navigates() {
        _state.update { change ->
            change.copy(isNavigate = null)
        }
    }

    private fun showError(message: String) {
        _state.update { error ->
            error.copy(error = "Login Failed, $message")
        }
    }

    fun showErrors() {
        _state.update { error ->
            error.copy(error = null)
        }
    }
}