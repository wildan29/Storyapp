package com.dicoding.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class RegisterDataModel(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
