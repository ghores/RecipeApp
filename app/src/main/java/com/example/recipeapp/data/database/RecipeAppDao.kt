package com.example.recipeapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.recipeapp.data.database.entity.DetailEntity
import com.example.recipeapp.data.database.entity.FavoriteEntity
import com.example.recipeapp.data.database.entity.RecipeEntity
import com.example.recipeapp.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    //Recipe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipes(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY id ASC")
    fun loadRecipes(): Flow<List<RecipeEntity>>

    //Detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDetail(detailEntity: DetailEntity)

    @Query("SELECT * FROM ${Constants.DETAIL_TABLE_NAME} WHERE id = :id")
    fun loadDetail(id: Int): Flow<DetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.DETAIL_TABLE_NAME} WHERE id = :id)")
    fun existsDetail(id: Int): Flow<Boolean>

    //Favorite
    @Insert(onConflict = REPLACE)
    suspend fun saveFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM ${Constants.FAVORITE_TABLE_NAME} ORDER BY id ASC")
    fun loadFavorite(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.FAVORITE_TABLE_NAME} WHERE id = :id)")
    fun existsFavorite(id: Int): Flow<Boolean>
}