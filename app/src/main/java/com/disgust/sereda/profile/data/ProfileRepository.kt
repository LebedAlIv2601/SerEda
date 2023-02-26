package com.disgust.sereda.profile.data

import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.profile.screens.profile.model.ProfileUser
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
class ProfileRepository @Inject constructor(
    private val firebaseDatabaseHelper: FirebaseDatabaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper,
    private val db: SerEdaDatabase
) {

    fun isAuth() = firebaseAuthHelper.isAuth()

    suspend fun getProfileInfo(): ProfileUser {
        return withContext(Dispatchers.IO) { firebaseDatabaseHelper.getUserData().toProfileUser() }
    }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            db.favoriteRecipeDao().clearFavoriteRecipes()
            firebaseAuthHelper.signOut()
        }
    }

    suspend fun addDiet(diet: String) =
        withContext(Dispatchers.IO) { firebaseDatabaseHelper.addDiet(diet) }

    suspend fun deleteDiet(diet: String) =
        withContext(Dispatchers.IO) { firebaseDatabaseHelper.deleteDiet(diet) }

    suspend fun addIntolerance(intolerance: String) =
        withContext(Dispatchers.IO) { firebaseDatabaseHelper.addIntolerance(intolerance) }

    suspend fun deleteIntolerance(intolerance: String) =
        withContext(Dispatchers.IO) { firebaseDatabaseHelper.deleteIntolerance(intolerance) }

    suspend fun getDiets(): List<String> =
        withContext(Dispatchers.IO) { firebaseDatabaseHelper.getDiets() }

    suspend fun getIntolerance(): List<String> =
        withContext(Dispatchers.IO) { firebaseDatabaseHelper.getIntolerance() }

}