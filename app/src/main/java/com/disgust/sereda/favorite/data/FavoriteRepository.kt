package com.disgust.sereda.favorite.data

import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.recipe.data.toFavoriteRecipeDBModel
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
class FavoriteRepository @Inject constructor(
    private val firebaseDatabaseHelper: FirebaseDatabaseHelper,
    private val authHelper: FirebaseAuthHelper,
    private val db: SerEdaDatabase
) {
    private val _favoriteRecipes = MutableStateFlow<List<FavoriteRecipe>>(listOf())
    val favoriteRecipes = _favoriteRecipes.asStateFlow()

    fun isAuth() = authHelper.isAuth()

    suspend fun getFavoriteRecipes() =
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().getFavoriteRecipesFlow()
                .map { it.map { item -> item.toFavoriteRecipe() } }
        }


    suspend fun updateLocalData(): List<FavoriteRecipe> {
        return withContext(Dispatchers.IO) {
            val recipesFromRemote =
                firebaseDatabaseHelper.getFavoriteRecipes().map { it.toFavoriteRecipeDBModel() }
            db.favoriteRecipeDao().updateFavoriteRecipes(recipesFromRemote)
            recipesFromRemote.map { it.toFavoriteRecipe() }
        }
    }

    suspend fun deleteRecipeFromFavorite(recipe: FavoriteRecipe) {
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
            firebaseDatabaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        }
    }
}