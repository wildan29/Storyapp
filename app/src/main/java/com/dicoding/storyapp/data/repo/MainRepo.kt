package com.dicoding.storyapp.data.repo

import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.domain.usecases.ApiHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepo @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun responseLogin(loginDataModel: LoginDataModel) =
        apiHelper.responseLogin(loginDataModel)

    suspend fun responseRegister(registerDataModel: RegisterDataModel) =
        apiHelper.responseRegister(registerDataModel)

    suspend fun getStories(token: String) = apiHelper.getStories(token)

    suspend fun getStoriesDetail(id: String, token: String) = apiHelper.getDetailStories(id, token)

    suspend fun addStories(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String
    ) = apiHelper.addStories(file, description, token)
}