package com.example.recipeapp.data.database

import androidx.room.TypeConverter
import com.example.recipeapp.models.recipe.ResponseRecipes
import com.google.gson.Gson

class RecipeAppTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun recipeToJson(recipe: ResponseRecipes): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): ResponseRecipes {
        return gson.fromJson(data, ResponseRecipes::class.java)
    }
}