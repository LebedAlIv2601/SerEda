package com.disgust.sereda.recipe.screens.search.interaction

sealed class RecipeFavoriteState {
    object NotFavorite : RecipeFavoriteState()
    object Loading : RecipeFavoriteState()
    object Favorite : RecipeFavoriteState()
}
