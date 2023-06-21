package com.dicoding.storyapp.domain.usecases

import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.data.models.LoginResponseDataModel
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.data.models.RegisterResponseDataModel
import retrofit2.Response

interface ApiHelper {
    suspend fun responseLogin(loginDataModel: LoginDataModel): Response<LoginResponseDataModel>

    suspend fun responseRegister(registerDataModel: RegisterDataModel): Response<RegisterResponseDataModel>
}