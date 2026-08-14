package com.example.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.database.entity.DetailEntity
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
        //Cache
        val cache = detailData.value?.data
        if (cache != null) {
            cacheDetail(cache.id!!, cache)
        }
    }

    //Local
    private fun saveDetail(detailEntity: DetailEntity) = viewModelScope.launch {
        recipeRepository.local.saveDetail(detailEntity)
    }

    fun readDetailFromDb(id: Int): LiveData<DetailEntity> = recipeRepository.local.loadDetail(id).asLiveData()

    val existsDetailData = MutableLiveData<Boolean>()
    fun existsDetail(id: Int) = viewModelScope.launch {
        recipeRepository.local.existsDetail(id).collect { existsDetailData.postValue(it) }
    }

    private fun cacheDetail(id: Int, responseDetail: ResponseDetail) {
        val detailEntity = DetailEntity(id, responseDetail)
        saveDetail(detailEntity)
    }

    //Similar
    val similarData = MutableLiveData<NetworkRequest<ResponseSimilar>>()
    fun callSimilarApi(id: Int, apiKey: String) = viewModelScope.launch {
        similarData.value = NetworkRequest.Loading()
        val response = recipeRepository.remote.getSimilarRecipes(id, apiKey)
        similarData.value = NetworkResponse(response).generalNetworkResponse()
    }
}