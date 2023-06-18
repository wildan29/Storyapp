package com.dicoding.storyapp.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}