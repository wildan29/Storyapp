package com.dicoding.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class AddStoryResponseDataModel(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
