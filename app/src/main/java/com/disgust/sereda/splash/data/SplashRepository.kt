package com.disgust.sereda.splash.data

import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import javax.inject.Inject

@ExperimentalMaterialApi
class SplashRepository @Inject constructor(private val firebaseHelper: FirebaseAuthHelper) {

    fun isAuth() = firebaseHelper.isAuth()

}