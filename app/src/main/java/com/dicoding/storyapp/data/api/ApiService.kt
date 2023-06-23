package com.dicoding.storyapp.data.api

import com.dicoding.storyapp.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun responseLogin(
        @Body loginDataModel: LoginDataModel
    ): Response<LoginResponseDataModel>

    @POST("register")
    suspend fun reponseRegister(
        @Body registerDataModel: RegisterDataModel
    ): Response<RegisterResponseDataModel>

    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): Response<AllStoriesResponse>

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String, @Header("Authorization") token: String
    ): Response<DetailStoryResponseDataModel>

    @Multipart
    @POST("stories")
    suspend fun addStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Response<AddStoryResponseDataModel>

}