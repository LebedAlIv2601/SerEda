package com.disgust.sereda.profile.data

import com.disgust.sereda.profile.screens.profile.model.ProfileUser
import com.disgust.sereda.utils.firebase.model.ProfileUserFirebaseModel

fun ProfileUserFirebaseModel.toProfileUser(): ProfileUser {
    return ProfileUser(
        email = email,
        favoriteRecipesCount = favoriteRecipes.count()
    )
}