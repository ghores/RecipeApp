package com.example.recipeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repository.RecipeRepository
import com.example.recipeapp.models.detail.ResponseDetail
import com.example.recipeapp.models.detail.ResponseSimilar
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val recipeRepository: RecipeRepository) : ViewModel() {
    //Api
    val detailData = MutableLiveData<NetworkRequest<ResponseDetail>>()
    fun callDetailApi(id: Int, apiKey: String) = viewModelScope.launch {
        detailData.value = NetworkRequest.Loading()
        val response = recipeRepository.remote.getDetail(id, apiKey)
        detailData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //Similar
    val similarData = MutableLiveData<NetworkRequest<ResponseSimilar>>()
    fun callSimilarApi(id: Int, apiKey: String) = viewModelScope.launch {
        similarData.value = NetworkRequest.Loading()
        val response = recipeRepository.remote.getSimilarRecipes(id, apiKey)
        similarData.value = NetworkResponse(response).generalNetworkResponse()
    }
}