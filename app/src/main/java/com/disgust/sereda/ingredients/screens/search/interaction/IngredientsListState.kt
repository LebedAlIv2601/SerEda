package com.disgust.sereda.ingredients.screens.search.interaction

import com.disgust.sereda.ingredients.screens.search.model.IngredientItem

sealed class IngredientsListState {
    object Waiting : IngredientsListState()
    object Loading : IngredientsListState()
    class Success(val data: List<IngredientItem>) : IngredientsListState()
    class Error(val exception: Exception) : IngredientsListState()
}