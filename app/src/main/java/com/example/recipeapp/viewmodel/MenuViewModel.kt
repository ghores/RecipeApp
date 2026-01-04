package com.example.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val menuRepository: MenuRepository) : ViewModel() {

    fun saveMenuData(mealTitle: String, mealId: Int, dietTitle: String, dietId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            menuRepository.saveMenuData(mealTitle, mealId, dietTitle, dietId)
        }

    val readMenuStoredItems = menuRepository.readMenuData

    fun mealsList(): MutableList<String> {
        return mutableListOf(
            "Main Course",
            "Bread",
            "Marinade",
            "Side Dish",
            "Breakfast",
            "Dessert",
            "Soup",
            "Snack",
            "Appetizer",
            "Beverage",
            "Drink",
            "Salad",
            "Sauce"
        )
    }

    fun dietsList(): MutableList<String> {
        return mutableListOf(
            "Gluten Free",
            "Ketogenic",
            "Vegetarian",
            "Vegan",
            "Pescetarian",
            "Paleo"
        )
    }
}