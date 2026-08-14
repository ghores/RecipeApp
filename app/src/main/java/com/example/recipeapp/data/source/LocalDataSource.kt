package com.example.recipeapp.data.source

import com.example.recipeapp.data.database.RecipeAppDao
import com.example.recipeapp.data.database.entity.DetailEntity
import com.example.recipeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {
    //Recipes
    suspend fun saveRecipes(recipeEntity: RecipeEntity) = dao.saveRecipes(recipeEntity)
    fun loadRecipes(): Flow<List<RecipeEntity>> = dao.loadRecipes()

    //Detail
    suspend fun saveDetail(detailEntity: DetailEntity) = dao.saveDetail(detailEntity)
    fun loadDetail(id: Int): Flow<DetailEntity> = dao.loadDetail(id)
    fun existsDetail(id: Int): Flow<Boolean> = dao.existsDetail(id)
}
