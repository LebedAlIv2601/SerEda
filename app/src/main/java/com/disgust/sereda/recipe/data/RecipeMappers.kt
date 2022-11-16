package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.search.interaction.RecipeFavoriteState
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.recipe.screens.search.model.RecipeItemResponse
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDBModel
import com.disgust.sereda.utils.firebase.model.FavoriteRecipeFirebaseModel

fun RecipeItemResponse.toRecipeItem(): RecipeItem {
    return RecipeItem(
        id = id,
        name = name,
        image = image,
        favoriteState = RecipeFavoriteState.NotFavorite
    )
}

fun FavoriteRecipeDBModel.toRecipeItem(): RecipeItem {
    return RecipeItem(
        id = id,
        name = name,
        image = image,
        favoriteState = RecipeFavoriteState.Favorite
    )
}

fun RecipeItem.toFavoriteRecipeDBModel(): FavoriteRecipeDBModel {
    return FavoriteRecipeDBModel(
        id = id,
        name = name,
        image = image
    )
}

fun RecipeItem.toFavoriteRecipeFirebaseModel(): FavoriteRecipeFirebaseModel {
    return FavoriteRecipeFirebaseModel(
        id = id.toString(),
        name = name,
        image = image
    )
}