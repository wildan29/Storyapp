package com.dicoding.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class LoginDataModel(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null
)
