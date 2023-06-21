package com.dicoding.storyapp.data.repo

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.data.models.LoginResponseDataModel
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.data.models.RegisterResponseDataModel
import com.dicoding.storyapp.domain.usecases.ApiHelper
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun responseLogin(loginDataModel: LoginDataModel): Response<LoginResponseDataModel> {
        return apiService.responseLogin(loginDataModel)
    }

    override suspend fun responseRegister(registerDataModel: RegisterDataModel): Response<RegisterResponseDataModel> {
        return apiService.reponseRegister(registerDataModel)
    }
}