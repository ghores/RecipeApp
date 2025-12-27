package com.example.recipeapp.data.network

import com.example.recipeapp.models.register.BodyRegister
import com.example.recipeapp.models.register.ResponseRegister
import com.example.recipeapp.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {
    @POST("users/connect")
    suspend fun postRegister(@Query(API_KEY) apiKey: String, @Body body: BodyRegister): Response<ResponseRegister>
}