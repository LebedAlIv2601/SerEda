package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.info.model.toRecipeInfo
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val api: RecipeApi
) {

    suspend fun getInfoRecipe(id: Int): RecipeInfo =
        api.getRecipeInfo(id).toRecipeInfo()

    suspend fun searchRecipes(query: String): List<RecipeItem> =
        api.searchRecipes(query).results

    suspend fun getRandomRecipes(): List<RecipeItem> =
        api.getRandomRecipes().recipes

}