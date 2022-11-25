package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.db.filters.FilterRecipeDBModel
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val api: RecipeApi,
    private val db: SerEdaDatabase,
    private val firebaseHelper: FirebaseDatabaseHelper
) {

    suspend fun getInfoRecipe(id: Int): RecipeInfo {
        return api.getRecipeInfo(id).toRecipeInfo()
    }

    suspend fun searchRecipes(
        query: String = "",
        sort: String = "",
        includeIngredients: String = "",
        excludeIngredients: String = ""
    ): List<RecipeItem> {
        val favoriteIds = getFavoriteRecipeIds()
        val recipes =
            api.searchRecipes(
                query,
                sort,
                includeIngredients,
                excludeIngredients
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

    fun getFavoriteRecipeIds(): List<Int> =
        db.favoriteRecipeDao().getFavoriteRecipes().map { it.id }


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

    fun getFiltersRecipe(): List<FilterRecipeDBModel> =
        db.filtersRecipeDao().getFilterRecipeByIngredients()

}