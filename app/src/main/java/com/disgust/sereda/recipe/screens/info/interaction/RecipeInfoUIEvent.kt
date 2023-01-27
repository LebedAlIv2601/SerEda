package com.disgust.sereda.recipe.screens.info.interaction

import com.disgust.sereda.utils.base.BaseUIEvent

sealed class RecipeInfoUIEvent : BaseUIEvent {
    class StartScreen(val id: Int, val state: Int) : RecipeInfoUIEvent()
    class ButtonRestartClick(val id: Int) : RecipeInfoUIEvent()
    object ButtonAddToFavoriteClick : RecipeInfoUIEvent()
    class IngredientItemClick(val id: Int, val name: String) : RecipeInfoUIEvent()

    class UserNotAuthDialogDismiss(val isConfirmed: Boolean) : RecipeInfoUIEvent()
}