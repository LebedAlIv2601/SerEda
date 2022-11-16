package com.disgust.sereda.utils.db.favoriteRecipes

import androidx.room.*

@Dao
interface FavoriteRecipeDao {

    @Query("SELECT * FROM favorite_recipes")
    fun getFavoriteRecipes(): List<FavoriteRecipeDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteRecipe(recipe: FavoriteRecipeDBModel)

    @Delete
    fun deleteFavoriteRecipe(recipe: FavoriteRecipeDBModel)
}