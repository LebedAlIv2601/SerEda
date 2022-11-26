package com.disgust.sereda.recipe.screens.info.interaction

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.utils.base.BaseState

sealed class RecipeInfoState : BaseState() {
    object Loading : RecipeInfoState()
    class Success(val data: RecipeInfo) : RecipeInfoState()
    class Error(val error: Exception) : RecipeInfoState()
}