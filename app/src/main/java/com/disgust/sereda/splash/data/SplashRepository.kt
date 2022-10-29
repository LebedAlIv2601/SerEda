package com.disgust.sereda.splash.data

import com.disgust.sereda.utils.FirebaseHelper
import javax.inject.Inject

class SplashRepository @Inject constructor(private val firebaseHelper: FirebaseHelper) {

    fun isAuth() = firebaseHelper.isAuth()

}