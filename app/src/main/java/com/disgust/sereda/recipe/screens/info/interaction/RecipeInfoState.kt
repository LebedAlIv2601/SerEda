package com.disgust.sereda.recipe.screens.info.interaction

import com.disgust.sereda.recipe.screens.info.model.RecipeInfoResponse

sealed class RecipeInfoState() {
    object Loading : RecipeInfoState()
    class Success(val data: RecipeInfoResponse) : RecipeInfoState()
    class Error(val error: Exception) : RecipeInfoState()
}