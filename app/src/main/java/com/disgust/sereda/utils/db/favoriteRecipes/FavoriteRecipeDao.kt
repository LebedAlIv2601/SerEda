package com.disgust.sereda.utils.db.favoriteRecipes

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favorite_recipes")
    fun getFavoriteRecipes(): List<FavoriteRecipeDBModel>

    @Query("SELECT * FROM favorite_recipes")
    fun getFavoriteRecipesFlow(): Flow<List<FavoriteRecipeDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteRecipe(recipe: FavoriteRecipeDBModel)

    @Delete
    fun deleteFavoriteRecipe(recipe: FavoriteRecipeDBModel)

    @Query("DELETE FROM favorite_recipes")
    fun clearFavoriteRecipes()

    @Query("DELETE FROM favorite_recipes WHERE id NOT IN (:actualRecipesIds)")
    fun deleteOldRecipes(actualRecipesIds: List<Int>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeList(recipes: List<FavoriteRecipeDBModel>)

    @Transaction
    fun updateFavoriteRecipes(recipes: List<FavoriteRecipeDBModel>) {
        insertRecipeList(recipes = recipes)
        deleteOldRecipes(recipes.map { it.id })
    }
}