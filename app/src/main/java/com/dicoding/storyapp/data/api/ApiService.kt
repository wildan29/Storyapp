package com.dicoding.storyapp.data.api

import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.data.models.LoginResponseDataModel
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.data.models.RegisterResponseDataModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun responseLogin(
        @Body loginDataModel: LoginDataModel
    ): Response<LoginResponseDataModel>

    @POST("register")
    suspend fun reponseRegister(
        @Body registerDataModel: RegisterDataModel
    ): Response<RegisterResponseDataModel>
}