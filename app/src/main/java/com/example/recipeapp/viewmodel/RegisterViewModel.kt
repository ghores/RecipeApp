package com.example.recipeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repository.RegisterRepository
import com.example.recipeapp.models.register.BodyRegister
import com.example.recipeapp.models.register.ResponseRegister
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) : ViewModel() {

    //Api
    val registerData = MutableLiveData<NetworkRequest<ResponseRegister>>()
    fun callRegisterApi(apikey: String, body: BodyRegister) = viewModelScope.launch {
        registerData.value = NetworkRequest.Loading()
        val response = registerRepository.postRegister(apikey, body)
        registerData.value = NetworkResponse(response).generalNetworkResponse()
    }

    //Stored data
    fun saveRegisterData(username: String, hash: String) = viewModelScope.launch {
        registerRepository.saveRegisterData(username, hash)
    }

    val readRegisterData = registerRepository.readRegisterData
}