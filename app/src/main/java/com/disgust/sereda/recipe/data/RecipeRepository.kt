package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.info.model.toRecipeInfo
import com.disgust.sereda.recipe.screens.search.interaction.RecipeFavoriteState
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val api: RecipeApi,
    private val db: SerEdaDatabase,
    private val firebaseHelper: FirebaseDatabaseHelper
) {

    private var favoriteIds = listOf<Int>()

    suspend fun getInfoRecipe(id: Int): RecipeInfo =
        api.getRecipeInfo(id).toRecipeInfo()

    suspend fun searchRecipes(query: String = "", sort: String = ""): List<RecipeItem> {
        val recipes =
            api.searchRecipes(query, sort).results.map { it.toRecipeItem() }.toMutableList()
        recipes.forEachIndexed { index, recipe ->
            val isFavorite = favoriteIds.find { it == recipe.id } != null
            if (isFavorite) {
                recipes[index] = recipe.copy(favoriteState = RecipeFavoriteState.Favorite)
            }
        }
        return recipes
    }

    suspend fun updateFavoriteRecipeIds() {
        firebaseHelper.getFavoriteRecipes { list ->
            favoriteIds = list.map { it.id.toInt() }
        }
    }

    suspend fun addFavoriteRecipe(recipe: RecipeItem) {
        firebaseHelper.addFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        db.favoriteRecipeDao().insertFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
    }

    suspend fun deleteFavoriteRecipe(recipe: RecipeItem) {
        firebaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
    }

}