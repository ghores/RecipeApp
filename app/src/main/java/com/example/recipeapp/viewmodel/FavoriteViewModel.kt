package com.example.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.recipeapp.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(favoriteRepository: FavoriteRepository) : ViewModel() {
    val readFavoriteData = favoriteRepository.local.loadFavorite().asLiveData()
}