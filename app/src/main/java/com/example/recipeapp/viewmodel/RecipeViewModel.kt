package com.example.recipeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repository.RecipeRepository
import com.example.recipeapp.models.recipe.ResponseRecipes
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) : ViewModel() {

    //Popular
    //Queries
    fun popularQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.API_KEY] = Constants.MY_API_KEY
        queries[Constants.SORT] = Constants.POPULARITY
        queries[Constants.NUMBER] = "10"
        queries[Constants.ADD_RECIPE_INFORMATION] = Constants.TRUE
        return queries
    }

    //Api
    val popularData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    fun callPopularApi(queries: Map<String, String>) = viewModelScope.launch {
        popularData.value = NetworkRequest.Loading()
        val response = recipeRepository.remote.getRecipes(queries)
        popularData.value = NetworkResponse(response).generalNetworkResponse()
    }
}