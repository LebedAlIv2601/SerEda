package com.disgust.sereda.favorite.favoriteList.interaction

import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class FavoriteUIEvent : BaseUIEvent {
    object StartScreen : FavoriteUIEvent()
    class FavoriteRecipesListItemClick(val recipe: FavoriteRecipe) : FavoriteUIEvent()
    class DeleteFromFavoriteButtonClick(val recipe: FavoriteRecipe) : FavoriteUIEvent()
    object UpdateButtonClick : FavoriteUIEvent()
    object ButtonAuthClick : FavoriteUIEvent()
}
