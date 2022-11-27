package com.disgust.sereda.favorite.data

import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.recipe.data.toFavoriteRecipeDBModel
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val firebaseDatabaseHelper: FirebaseDatabaseHelper,
    private val db: SerEdaDatabase
) {
    private val _favoriteRecipes = MutableStateFlow<List<FavoriteRecipe>>(listOf())
    val favoriteRecipes = _favoriteRecipes.asStateFlow()

    fun getFavoriteRecipes() =
        db.favoriteRecipeDao().getFavoriteRecipesFlow()
            .map { it.map { item -> item.toFavoriteRecipe() } }


    suspend fun updateLocalData(): List<FavoriteRecipe> {
        val recipesFromRemote =
            firebaseDatabaseHelper.getFavoriteRecipes().map { it.toFavoriteRecipeDBModel() }
        db.favoriteRecipeDao().updateFavoriteRecipes(recipesFromRemote)
        return recipesFromRemote.map { it.toFavoriteRecipe() }
    }

    fun deleteRecipeFromFavorite(recipe: FavoriteRecipe) {
        db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
        firebaseDatabaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
    }
}