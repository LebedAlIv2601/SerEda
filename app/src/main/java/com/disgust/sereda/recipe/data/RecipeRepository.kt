package com.disgust.sereda.recipe.data

import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.commonModel.IngredientFilter
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
        return withContext(Dispatchers.IO) { api.getRecipeInfo(id).toRecipeInfo() }
    }

    suspend fun searchRecipes(
        query: String = "",
        sort: String = "",
        includeIngredients: String = "",
        excludeIngredients: String = "",
        diet: String = "",
        intolerances: String = "",
        maxReadyTime: Int? = null,
        minCalories: Int? = null,
        maxCalories: Int? = null,
        offset: Int = 0
    ): List<RecipeItem> {
        return withContext(Dispatchers.IO) {
            val favoriteIds = getFavoriteRecipeIds()
            val recipes =
                api.searchRecipes(
                    query,
                    sort,
                    includeIngredients,
                    excludeIngredients,
                    diet,
                    intolerances,
                    maxReadyTime,
                    minCalories,
                    maxCalories,
                    offset
                ).results.map { it.toRecipeItem() }.toMutableList()
            recipes.forEachIndexed { index, recipe ->
                val isFavorite = favoriteIds.find { it == recipe.id } != null
                if (isFavorite) {
                    recipes[index] = recipe.copy(favoriteState = RecipeFavoriteState.FAVORITE)
                }
            }
            recipes
        }
    }

    suspend fun updateFavoriteRecipeIds() {
        withContext(Dispatchers.IO) {
            val list = firebaseHelper.getFavoriteRecipes()
            db.favoriteRecipeDao()
                .updateFavoriteRecipes(list.map { it.toFavoriteRecipeDBModel() })
        }
    }

    fun getFavoriteRecipeIdsFlow() =
        db.favoriteRecipeDao().getFavoriteRecipesFlow().map { it.map { item -> item.id } }

    //add recipe to favorite
    suspend fun addFavoriteRecipe(recipe: RecipeItem) {
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().insertFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
            firebaseHelper.addFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        }
    }

    suspend fun addFavoriteRecipe(recipe: RecipeInfo) {
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().insertFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
            firebaseHelper.addFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        }
    }
    //end

    //delete recipe from favorite
    suspend fun deleteFavoriteRecipe(recipe: RecipeItem) {
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
            firebaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        }
    }

    suspend fun deleteFavoriteRecipe(recipe: RecipeInfo) {
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().deleteFavoriteRecipe(recipe.toFavoriteRecipeDBModel())
            firebaseHelper.deleteFavoriteRecipe(recipe.toFavoriteRecipeFirebaseModel())
        }
    }
    //end

    suspend fun getFiltersIngredientRecipe(): List<IngredientFilter> =
        withContext(Dispatchers.IO) {
            db.filtersRecipeDao().getFiltersRecipe().map { it.toIngredientFilter() }
        }

    suspend fun deleteAllFiltersRecipe() =
        withContext(Dispatchers.IO) {
            db.filtersRecipeDao().deleteAllFiltersRecipe()
        }

    suspend fun deleteFiltersIngredient(ingredientFilter: IngredientFilter) =
        withContext(Dispatchers.IO) {
            db.filtersRecipeDao().deleteFilterRecipe(ingredientFilter.toFilterRecipeDBModel())
        }

    suspend fun updateFiltersIngredients(list: List<IngredientFilter>) =
        withContext(Dispatchers.IO) {
            db.filtersRecipeDao()
                .updateFilterRecipeByIngredient(list.map { it.toFilterRecipeDBModel() })
        }

    suspend fun getDiets(): List<String> = firebaseHelper.getDiets()

    suspend fun getIntolerance(): List<String> = firebaseHelper.getIntolerance()

    private suspend fun getFavoriteRecipeIds(): List<Int> =
        withContext(Dispatchers.IO) { db.favoriteRecipeDao().getFavoriteRecipes().map { it.id } }
}