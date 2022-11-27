package com.disgust.sereda.auth.googleAuth

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.auth.data.AuthRepository
import com.disgust.sereda.auth.googleAuth.interaction.GoogleAuthUIEvent
import com.disgust.sereda.auth.googleAuth.interaction.OneTapSignInState
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class GoogleAuthViewModel @Inject constructor(private val repository: AuthRepository) :
    NavigatorViewModel(), UIEventHandler<GoogleAuthUIEvent> {

    private val oneTapSignInState = repository.oneTapSignInState

    override fun onUIEvent(event: GoogleAuthUIEvent) {
        when (event) {
            is GoogleAuthUIEvent.ButtonAuthClick -> {
                oneTapSignIn(event.launcher)
            }
            is GoogleAuthUIEvent.IntentResultOk -> {
                signInWithGoogle(event.intent)
            }
        }
    }

    private fun oneTapSignIn(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        doSingleRequest(
            query = { repository.oneTapSignIn() },
            doOnSuccess = { launchAuthDialog(launcher) }
        )
    }

    private fun signInWithGoogle(intent: Intent?) {
        val credentials = repository.getCredentialFromIntent(intent)
        val googleIdToken = credentials.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        doSingleRequest(
            query = { repository.signInWithGoogle(googleCredentials) },
            doOnSuccess = { navigateWithClearBackStack(Screen.SearchRecipe.route) }
        )
    }

    private fun launchAuthDialog(
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) {
        oneTapSignInState.value.doAsStateIfPossible<OneTapSignInState.Success> {
            val intent = IntentSenderRequest.Builder(it.signInResult.pendingIntent.intentSender)
                .build()
            launcher.launch(intent)
        }
    }
}