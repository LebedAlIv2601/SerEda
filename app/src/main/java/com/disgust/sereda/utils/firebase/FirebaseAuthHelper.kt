package com.disgust.sereda.utils.firebase

import android.content.Intent
import com.disgust.sereda.utils.Constants
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.MainActivity
import com.disgust.sereda.utils.firebase.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Named


class FirebaseAuthHelper(
    private val oneTapClient: SignInClient,
    database: FirebaseDatabase,
    @Named(Constants.SIGN_IN_REQUEST)
    private val signInRequest: BeginSignInRequest,
    @Named(Constants.SIGN_UP_REQUEST)
    private val signUpRequest: BeginSignInRequest
) {
@ExperimentalMaterialApi
class FirebaseAuthHelper {
    private val auth = Firebase.auth
    private val usersDatabaseReference: DatabaseReference = database.reference.child("users")

    fun isAuth() = auth.currentUser != null

    fun signOut() {
        auth.signOut()
    }

    suspend fun oneTapSignInWithGoogle(
        onSuccess: (BeginSignInResult) -> Unit
    ) {
        try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            onSuccess.invoke(signInResult)
        } catch (e: Exception) {
            val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
            onSuccess.invoke(signUpResult)
        }
    }

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential, onSuccess: () -> Unit) {
        val authResult = auth.signInWithCredential(googleCredential).await()
        val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
        if (isNewUser) {
            authUser(auth.currentUser)
        }
        onSuccess()
    }

    fun getCredentialFromIntent(intent: Intent?) =
        oneTapClient.getSignInCredentialFromIntent(intent)

    private suspend fun authUser(user: FirebaseUser?) {
        if (user != null) {
            usersDatabaseReference.child(user.uid)
                .setValue(User(user.uid, user.email ?: "null")).await()
        }
    }
}