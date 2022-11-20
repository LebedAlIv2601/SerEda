package com.disgust.sereda.auth.googleAuth

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import com.disgust.sereda.auth.data.AuthRepository
import com.disgust.sereda.auth.googleAuth.interaction.GoogleAuthUIEvent
import com.disgust.sereda.auth.googleAuth.interaction.OneTapSignInState
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.navigation.navigateWithClearBackStack
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class GoogleAuthViewModel @Inject constructor(private val repository: AuthRepository) :
    ViewModel(), UIEventHandler<GoogleAuthUIEvent> {

    private val oneTapSignInState = repository.oneTapSignInState

    override fun onUIEvent(event: GoogleAuthUIEvent) {
        when (event) {
            is GoogleAuthUIEvent.ButtonAuthClick -> {
                oneTapSignIn(event.launcher)
            }
            is GoogleAuthUIEvent.IntentResultOk -> {
                signInWithGoogle(event.intent) {
                    event.navController.navigateWithClearBackStack(Screen.Screen1.route)
                }
            }
        }
    }

    private fun oneTapSignIn(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        doSingleRequest(
            query = { repository.oneTapSignIn() },
            doOnSuccess = { launchAuthDialog(launcher) },
            doOnError = { it.message?.let { it1 -> Log.e("gh", it1) } }
        )
    }

    private fun signInWithGoogle(intent: Intent?, onSuccess: () -> Unit) {
        val credentials = repository.getCredentialFromIntent(intent)
        val googleIdToken = credentials.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        doSingleRequest(
            query = { repository.signInWithGoogle(googleCredentials) },
            doOnSuccess = { onSuccess.invoke() }
        )
    }

    private fun launchAuthDialog(
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) {
        when (val oneTapState = oneTapSignInState.value) {
            is OneTapSignInState.Success -> {
                val intent =
                    IntentSenderRequest.Builder(oneTapState.signInResult.pendingIntent.intentSender)
                        .build()
                launcher.launch(intent)
            }
            else -> {}
        }
    }
}