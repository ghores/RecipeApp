package com.example.recipeapp.data.repository

import com.example.recipeapp.data.source.RemoteDataSource
import com.example.recipeapp.models.register.BodyRegister
import com.example.recipeapp.models.register.ResponseRegister
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun postRegister(apikey: String, body: BodyRegister): Response<ResponseRegister> = remoteDataSource.postRegister(apikey, body)
}