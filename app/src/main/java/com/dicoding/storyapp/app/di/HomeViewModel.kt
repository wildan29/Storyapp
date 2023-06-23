package com.dicoding.storyapp.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.di.LoginSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginSession: LoginSession
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            loginSession.updateLoginSession("")
        }
    }

}