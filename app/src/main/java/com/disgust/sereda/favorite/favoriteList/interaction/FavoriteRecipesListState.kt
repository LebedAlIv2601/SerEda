package com.disgust.sereda.favorite.favoriteList.interaction

import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.utils.base.BaseState

sealed class FavoriteRecipesListState : BaseState() {
    object Waiting : FavoriteRecipesListState()
    object Loading : FavoriteRecipesListState()
    class Updated(val data: List<FavoriteRecipe>) : FavoriteRecipesListState()
    class Updating(val data: List<FavoriteRecipe>) : FavoriteRecipesListState()
    class NotUpdated(val data: List<FavoriteRecipe>) : FavoriteRecipesListState()
    class Error(val exception: Exception) : FavoriteRecipesListState()
}

//inline fun <reified T> FavoriteRecipesListState.isState(
//    doIfTrue: (T) -> Unit
//) {
//    if (this is T){
//        doIfTrue(this)
//    }
//}
