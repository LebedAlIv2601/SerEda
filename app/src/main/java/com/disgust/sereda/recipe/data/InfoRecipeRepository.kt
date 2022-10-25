package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfoResponse
import javax.inject.Inject

class InfoRecipeRepository @Inject constructor(
    private val api: InfoRecipeApi
) {

    suspend fun getInfoRecipe(id: Int): RecipeInfoResponse =
        api.getRecipeInfo(id)

}