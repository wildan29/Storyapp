package com.dicoding.storyapp.app.utils

import android.view.View
import com.dicoding.storyapp.BuildConfig

object Utils {
    const val BASE_URL = BuildConfig.BASE_URL
    const val IS_DEBUG = BuildConfig.BUILD_TYPE == "debug"

    const val GET_EMAIL_KEY = "GET_EMAIL_KEY"
    const val GET_PASSWORD_KEY = "GET_PASSWORD_KEY"
    const val GET_REQ_EMAIL = "GET_REQ_KEY"
    const val GET_REQ_PASSWORD = "GET_PASSWORD_KEY"

    fun showLoading(view: View, isLoading: Boolean) =
        if (isLoading) view.visibility = View.VISIBLE
        else view.visibility = View.INVISIBLE
}