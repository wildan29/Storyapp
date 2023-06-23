package com.dicoding.storyapp.data.api

import com.dicoding.storyapp.data.models.*
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

}