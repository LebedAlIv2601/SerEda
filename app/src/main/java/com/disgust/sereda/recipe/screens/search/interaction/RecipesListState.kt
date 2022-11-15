package com.disgust.sereda.recipe.screens.search.interaction

import com.disgust.sereda.recipe.screens.search.model.RecipeItem

sealed class RecipesListState {
    object Waiting : RecipesListState()
    object Loading : RecipesListState()
    class Success(val data: List<RecipeItem>) : RecipesListState()
    class Error(val exception: Exception) : RecipesListState()
}