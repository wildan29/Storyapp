package com.dicoding.storyapp.data.di

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.models.StoryDatabase
import com.dicoding.storyapp.data.repo.StoryRepositoryImpl
import com.dicoding.storyapp.domain.usecases.StoryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton
    @Provides
    fun provideStoryRepository(
        database: StoryDatabase,
        api: ApiService,
        session: LoginSession
    ): StoryRepo = StoryRepositoryImpl(database, api, session)
}