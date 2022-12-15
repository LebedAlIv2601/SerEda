package com.disgust.sereda.ingredients.screens.search.interaction

import com.disgust.sereda.ingredients.screens.search.model.IngredientItem
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class IngredientsListUIEvent : BaseUIEvent {
    class SearchClick(val query: String) : IngredientsListUIEvent()
    class ListItemClick(val item: IngredientItem) :
        IngredientsListUIEvent()

    class InputTextChange(val text: String) : IngredientsListUIEvent()
    object KeyboardInitShow : IngredientsListUIEvent()
    class ListScrolledToLoadMoreDataPosition(val loadedItems: Int) : IngredientsListUIEvent()
}