package com.example.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.database.RecipeEntity
import com.example.recipeapp.data.repository.RecipeRepository
import com.example.recipeapp.models.recipe.ResponseRecipes
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) : ViewModel() {

    //---Popular---//
    //Queries
    fun popularQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.SORT] = Constants.POPULARITY
        queries[Constants.NUMBER] = Constants.LIMITED_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    //Api
    val popularData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callPopularApi(queries: Map<String, String>) = viewModelScope.launch {
        popularData.value = NetworkRequest.Loading()
        val response = recipeRepository.remote.getRecipes(queries)
        popularData.value = NetworkResponse(response).generalNetworkResponse()
        //Cache
        val cache = popularData.value?.data
        if (cache != null) offlinePopular(cache)
    }

    //Local
    private fun savePopular(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepository.local.saveRecipes(recipeEntity)
    }

    val readPopularFromDb: LiveData<List<RecipeEntity>> = recipeRepository.local.loadRecipes().asLiveData()

    private fun offlinePopular(responseRecipes: ResponseRecipes) {
        val recipeEntity = RecipeEntity(0, responseRecipes)
        savePopular(recipeEntity)
    }

    //---Recent---//
    //Queries
    fun recentQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.TYPE] = Constants.MAIN_COURSE
        queries[Constants.DIET] = Constants.GLUTEN_FREE
        queries[Constants.NUMBER] = Constants.FULL_COUNT.toString()
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    //Api
    val recentData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callRecentApi(queries: Map<String, String>) = viewModelScope.launch {
        recentData.value = NetworkRequest.Loading()
        val response = recipeRepository.remote.getRecipes(queries)
        recentData.value = NetworkResponse(response).generalNetworkResponse()
    }

    private fun recentNetworkResponse(response: Response<ResponseRecipes>): NetworkRequest<ResponseRecipes> {
        return when {
            response.message().contains("timeout") -> NetworkRequest.Error("Timeout")
            response.code() == 401 -> NetworkRequest.Error("You are not authorized")
            response.code() == 402 -> NetworkRequest.Error("Your free plan finished")
            response.code() == 422 -> NetworkRequest.Error("Api key not found!")
            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.body()!!.results.isNullOrEmpty() -> NetworkRequest.Error("Not found any recipe!")
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}