package com.disgust.sereda.auth.data

import android.content.Intent
import com.disgust.sereda.auth.googleAuth.interaction.OneTapSignInState
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseHelper: FirebaseAuthHelper) {

    private val _oneTapSignInState = MutableStateFlow<OneTapSignInState>(OneTapSignInState.Waiting)
    val oneTapSignInState = _oneTapSignInState.asStateFlow()

    suspend fun oneTapSignIn() {
        _oneTapSignInState.value = OneTapSignInState.Loading
        firebaseHelper.oneTapSignInWithGoogle {
            _oneTapSignInState.value = OneTapSignInState.Success(it)
        }
    }

    suspend fun signInWithGoogle(googleCredential: AuthCredential) {
        firebaseHelper.firebaseSignInWithGoogle(googleCredential) {

        }
    }

    fun getCredentialFromIntent(intent: Intent?) =
        firebaseHelper.getCredentialFromIntent(intent)

}