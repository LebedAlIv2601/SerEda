package com.disgust.sereda.utils.firebase.model

data class ProfileUserFirebaseModel(
    val email: String,
    val favoriteRecipes: List<FavoriteRecipeFirebaseModel>
)
