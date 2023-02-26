package com.disgust.sereda.auth.data

import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import com.disgust.sereda.auth.googleAuth.interaction.OneTapSignInState
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
class AuthRepository @Inject constructor(private val firebaseHelper: FirebaseAuthHelper) {

    private val _oneTapSignInState = MutableStateFlow<OneTapSignInState>(OneTapSignInState.Waiting)
    val oneTapSignInState = _oneTapSignInState.asStateFlow()

    suspend fun oneTapSignIn() {
        withContext(Dispatchers.IO) {
            _oneTapSignInState.value = OneTapSignInState.Loading
            firebaseHelper.oneTapSignInWithGoogle {
                _oneTapSignInState.value = OneTapSignInState.Success(it)
            }
        }
    }

    suspend fun signInWithGoogle(googleCredential: AuthCredential) {
        withContext(Dispatchers.IO) {
            firebaseHelper.firebaseSignInWithGoogle(googleCredential) {}
        }
    }

    fun getCredentialFromIntent(intent: Intent?) =
        firebaseHelper.getCredentialFromIntent(intent)

}