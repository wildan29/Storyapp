package com.dicoding.storyapp.domain.usecases

import com.dicoding.storyapp.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface ApiHelper {
    suspend fun responseLogin(loginDataModel: LoginDataModel): Response<LoginResponseDataModel>

    suspend fun responseRegister(registerDataModel: RegisterDataModel): Response<RegisterResponseDataModel>

    suspend fun getStories(token: String): Response<AllStoriesResponse>

    suspend fun getDetailStories(id: String, token: String): Response<DetailStoryResponseDataModel>

    suspend fun addStories(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String
    ): Response<AddStoryResponseDataModel>
}