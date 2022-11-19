package com.disgust.sereda.recipe.screens.info.interaction

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo

sealed class RecipeInfoState {
    object Loading : RecipeInfoState()
    class Success(val data: RecipeInfo) : RecipeInfoState()
    class Error(val error: Exception) : RecipeInfoState()
}