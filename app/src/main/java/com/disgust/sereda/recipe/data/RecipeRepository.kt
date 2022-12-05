package com.disgust.sereda.recipe.data

import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.search.model.IngredientFilter
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalMaterialApi
@Singleton
class RecipeRepository @Inject constructor(
    private val api: RecipeApi,
    private val db: SerEdaDatabase,
    private val firebaseHelper: FirebaseDatabaseHelper,
    private val authHelper: FirebaseAuthHelper
) {

    fun isAuth() = authHelper.isAuth()

    suspend fun getInfoRecipe(id: Int): RecipeInfo {
        return api.getRecipeInfo(id).toRecipeInfo()
    }

    suspend fun searchRecipes(
        query: String = "",
        sort: String = "",
        includeIngredients: String = "",
        excludeIngredients: String = "",
        diet: String = ""
    ): List<RecipeItem> {
        val favoriteIds = getFavoriteRecipeIds()
        val recipes =
            api.searchRecipes(
                query,
                sort,
                includeIngredients,
                excludeIngredients,
                diet
            ).results.map { it.toRecipeItem() }.toMutableList()
        recipes.forEachIndexed { index, recipe ->
            val isFavorite = favoriteIds.find { it == recipe.id } != null
            if (isFavorite) {
                recipes[index] = recipe.copy(favoriteState = RecipeFavoriteState.FAVORITE)
            }
        }
        return recipes
    }

    suspend fun updateFavoriteRecipeIds() {
        val list = firebaseHelper.getFavoriteRecipes()
        db.favoriteRecipeDao()
            .updateFavoriteRecipes(list.map { it.toFavoriteRecipeDBModel() })
    }

    fun getFavoriteRecipeIdsFlow() =
        db.favoriteRecipeDao().getFavoriteRecipesFlow().map { it.map { item -> item.id } }

    //add recipe to favorite
    fun addFavoriteRecipe(recipe: RecipeItem) {
        db.favoriteRecipeDao().insertFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
        firebaseHelper.addFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
    }

    fun addFavoriteRecipe(recipe: RecipeInfo) {
        db.favoriteRecipeDao().insertFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
        firebaseHelper.addFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
    }
    //end

    //delete recipe from favorite
    fun deleteFavoriteRecipe(recipe: RecipeItem) {
        db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
        firebaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
    }

    fun deleteFavoriteRecipe(recipe: RecipeInfo) {
        db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
        firebaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
    }
    //end

    fun getFiltersIngredientsRecipe(): List<IngredientFilter> =
        db.filtersRecipeDao().getFilterRecipeByIngredients().map {
            it.toIngredientFilter()
        }

    private fun getFavoriteRecipeIds(): List<Int> =
        db.favoriteRecipeDao().getFavoriteRecipes().map { it.id }
}