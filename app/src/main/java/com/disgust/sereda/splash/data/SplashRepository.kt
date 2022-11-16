package com.disgust.sereda.splash.data

import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import javax.inject.Inject

class SplashRepository @Inject constructor(private val firebaseHelper: FirebaseAuthHelper) {

    fun isAuth() = firebaseHelper.isAuth()

}