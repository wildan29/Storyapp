package com.dicoding.storyapp.data.repo

import com.dicoding.storyapp.data.models.LoginDataModel
import com.dicoding.storyapp.data.models.RegisterDataModel
import com.dicoding.storyapp.domain.usecases.ApiHelper
import javax.inject.Inject

class MainRepo @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun responseLogin(loginDataModel: LoginDataModel) =
        apiHelper.responseLogin(loginDataModel)

    suspend fun responseRegister(registerDataModel: RegisterDataModel) =
        apiHelper.responseRegister(registerDataModel)
}