package com.example.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {

    fun mealsList(): MutableList<String> {
        return mutableListOf(
            "Main Course", "Bread", "Marinade", "Side Dish", "Breakfast", "Dessert", "Soup", "Snack", "Appetizer",
            "Beverage", "Drink", "Salad", "Sauce"
        )
    }

    fun dietsList(): MutableList<String> {
        return mutableListOf("Gluten Free", "Ketogenic", "Vegetarian", "Vegan", "Pescetarian", "Paleo")
    }
}