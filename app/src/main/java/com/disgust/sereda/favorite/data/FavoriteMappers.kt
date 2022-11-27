package com.disgust.sereda.favorite.data

import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDBModel
import com.disgust.sereda.utils.firebase.model.FavoriteRecipeFirebaseModel

fun FavoriteRecipeDBModel.toFavoriteRecipe(): FavoriteRecipe {
    return FavoriteRecipe(
        id = id,
        name = name,
        image = image
    )
}

fun FavoriteRecipe.toFavoriteRecipeDBModel(): FavoriteRecipeDBModel {
    return FavoriteRecipeDBModel(
        id = id,
        name = name,
        image = image
    )
}

fun FavoriteRecipe.toFavoriteRecipeFirebaseModel(): FavoriteRecipeFirebaseModel {
    return FavoriteRecipeFirebaseModel(
        id = id.toString(),
        name = name,
        image = image
    )
}