package com.example.recipeapp.data.repository

import com.example.recipeapp.data.source.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class LuckyRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getRandomRecipe(queries: Map<String, String>) = remoteDataSource.getRandomRecipe(queries)
}