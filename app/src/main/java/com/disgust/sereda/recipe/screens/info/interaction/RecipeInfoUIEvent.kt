package com.disgust.sereda.recipe.screens.info.interaction

import com.disgust.sereda.utils.base.BaseUIEvent

sealed class RecipeInfoUIEvent : BaseUIEvent {
    class StartScreen(val id: Int) : RecipeInfoUIEvent()
    class ButtonRestartClick(val id: Int) : RecipeInfoUIEvent()
}