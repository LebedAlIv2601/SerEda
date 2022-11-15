package com.disgust.sereda.recipe.screens.search.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class RecipesListUIEvent : BaseUIEvent {
    class SearchClick(val query: String) : RecipesListUIEvent()
    class ListItemClick(val item: RecipeItem, val navController: NavHostController) :
        RecipesListUIEvent()

    class InputTextChange(val text: String) : RecipesListUIEvent()
    object KeyboardInitShow : RecipesListUIEvent()
    object StartScreen : RecipesListUIEvent()
}