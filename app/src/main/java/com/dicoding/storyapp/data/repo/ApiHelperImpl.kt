package com.dicoding.storyapp.data.repo

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.models.*
import com.dicoding.storyapp.domain.usecases.ApiHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun responseLogin(loginDataModel: LoginDataModel): Response<LoginResponseDataModel> {
        return apiService.responseLogin(loginDataModel)
    }

    override suspend fun responseRegister(registerDataModel: RegisterDataModel): Response<RegisterResponseDataModel> {
        return apiService.reponseRegister(registerDataModel)
    }

    override suspend fun getStories(token: String): Response<AllStoriesResponse> {
        return apiService.getStories(token)
    }

    override suspend fun getDetailStories(
        id: String,
        token: String
    ): Response<DetailStoryResponseDataModel> {
        return apiService.getStoryDetail(id, token)
    }

    override suspend fun addStories(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String
    ): Response<AddStoryResponseDataModel> {
        return apiService.addStories(file, description, token)
    }
}