package com.disgust.sereda.profile.data

import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.profile.screens.profile.model.ProfileUser
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import javax.inject.Inject

@ExperimentalMaterialApi
class ProfileRepository @Inject constructor(
    private val firebaseDatabaseHelper: FirebaseDatabaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper,
    private val db: SerEdaDatabase
) {

    fun isAuth() = firebaseAuthHelper.isAuth()

    suspend fun getProfileInfo(): ProfileUser {
        return firebaseDatabaseHelper.getUserData().toProfileUser()
    }

    fun signOut() {
        db.favoriteRecipeDao().clearFavoriteRecipes()
        firebaseAuthHelper.signOut()
    }
}