package com.example.recipeapp.data.source

import com.example.recipeapp.data.database.RecipeAppDao
import com.example.recipeapp.data.database.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {
    //Recipes
    suspend fun saveRecipes(recipeEntity: RecipeEntity) = dao.saveRecipes(recipeEntity)
    fun loadRecipes(): Flow<List<RecipeEntity>> = dao.loadRecipes()
}
