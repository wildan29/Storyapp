package com.dicoding.storyapp.domain.usecases

import com.dicoding.storyapp.data.models.*
import retrofit2.Response

interface ApiHelper {
    suspend fun responseLogin(loginDataModel: LoginDataModel): Response<LoginResponseDataModel>

    suspend fun responseRegister(registerDataModel: RegisterDataModel): Response<RegisterResponseDataModel>

    suspend fun getStories(token: String): Response<AllStoriesResponse>

    suspend fun getDetailStories(id: String, token: String): Response<DetailStoryResponseDataModel>
}