package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.info.model.toRecipeInfo
import javax.inject.Inject

class InfoRecipeRepository @Inject constructor(
    private val api: InfoRecipeApi
) {

    suspend fun getInfoRecipe(id: Int): RecipeInfo =
        api.getRecipeInfo(id).toRecipeInfo()

}