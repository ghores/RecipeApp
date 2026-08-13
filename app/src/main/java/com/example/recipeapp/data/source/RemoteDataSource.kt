package com.example.recipeapp.data.source

import com.example.recipeapp.data.network.ApiServices
import com.example.recipeapp.models.detail.ResponseDetail
import com.example.recipeapp.models.detail.ResponseSimilar
import com.example.recipeapp.models.recipe.ResponseRecipes
import com.example.recipeapp.models.register.BodyRegister
import com.example.recipeapp.models.register.ResponseRegister
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiServices: ApiServices) {
    suspend fun postRegister(apikey: String, body: BodyRegister): Response<ResponseRegister> = apiServices.postRegister(apikey, body)
    suspend fun getRecipes(queries: Map<String, String>): Response<ResponseRecipes> = apiServices.getRecipes(queries)
    suspend fun getDetail(id: Int, apikey: String): Response<ResponseDetail> = apiServices.getDetail(id, apikey)
    suspend fun getSimilarRecipes(id: Int, apikey: String): Response<ResponseSimilar> = apiServices.getSimilarRecipes(id, apikey)
}