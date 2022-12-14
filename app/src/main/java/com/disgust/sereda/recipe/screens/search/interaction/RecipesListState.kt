package com.disgust.sereda.recipe.screens.search.interaction

import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.base.BaseState
import com.disgust.sereda.utils.components.PagingState

sealed class RecipesListState : BaseState() {
    object Waiting : RecipesListState()
    object Loading : RecipesListState()
    class Success(val data: List<RecipeItem>, val pagingState: PagingState = PagingState.Waiting) :
        RecipesListState()

    class Error(val exception: Exception) : RecipesListState()
}