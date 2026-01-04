package com.example.recipeapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    //Recipe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY ID ASC")
    suspend fun loadRecipe(): Flow<List<RecipeEntity>>
}