package com.disgust.sereda.ingredients.screens.info.interactioin

import com.disgust.sereda.utils.base.BaseUIEvent

sealed class IngredientInfoUIEvent : BaseUIEvent {
    class StartScreen(val id: Int) : IngredientInfoUIEvent()
    class ButtonRestartClick(val id: Int) : IngredientInfoUIEvent()
}