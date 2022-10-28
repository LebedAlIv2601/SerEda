package com.disgust.sereda.ingredients.screens.search.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.ingredients.screens.search.model.IngredientItem
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class IngredientsListUIEvent : BaseUIEvent {
    class SearchClick(val query: String) : IngredientsListUIEvent()
    class ListItemClick(val item: IngredientItem, val navController: NavHostController) :
        IngredientsListUIEvent()

    class InputTextChange(val text: String) : IngredientsListUIEvent()
    object KeyboardInitShow : IngredientsListUIEvent()
}