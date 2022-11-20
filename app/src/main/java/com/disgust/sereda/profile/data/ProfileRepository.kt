package com.disgust.sereda.profile.data

import com.disgust.sereda.profile.screens.profile.model.ProfileUser
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.disgust.sereda.utils.firebase.FirebaseDatabaseHelper
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseDatabaseHelper: FirebaseDatabaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper
) {

    suspend fun getProfileInfo(): ProfileUser {
        return firebaseDatabaseHelper.getUserData().toProfileUser()
    }

    fun signOut() {
        firebaseAuthHelper.signOut()
    }
}