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

    fun addDiet(diet: String) = firebaseDatabaseHelper.addDiet(diet)

    fun deleteDiet(diet: String) = firebaseDatabaseHelper.deleteDiet(diet)

    fun addIntolerance(intolerance: String) = firebaseDatabaseHelper.addIntolerance(intolerance)

    fun deleteIntolerance(intolerance: String) =
        firebaseDatabaseHelper.deleteIntolerance(intolerance)

    suspend fun getDiets(): List<String> = firebaseDatabaseHelper.getDiets()

    suspend fun getIntolerance(): List<String> = firebaseDatabaseHelper.getIntolerance()

}