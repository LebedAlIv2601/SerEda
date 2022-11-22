package com.disgust.sereda.ingredients.screens.info.interactioin

import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class IngredientInfoUIEvent : BaseUIEvent {
    class StartScreen(val id: Int, val name: String) : IngredientInfoUIEvent()
    class ButtonRestartClick(val id: Int) : IngredientInfoUIEvent()

    class IngredientAddButtonClick(val navController: NavHostController) : IngredientInfoUIEvent()
    class IngredientExcludeButtonClick(val navController: NavHostController) :
        IngredientInfoUIEvent()
}