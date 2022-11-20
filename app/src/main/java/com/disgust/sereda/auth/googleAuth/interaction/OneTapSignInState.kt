package com.disgust.sereda.auth.googleAuth.interaction

import com.google.android.gms.auth.api.identity.BeginSignInResult

sealed class OneTapSignInState {
    object Waiting : OneTapSignInState()
    object Loading : OneTapSignInState()
    class Success(val signInResult: BeginSignInResult) : OneTapSignInState()
}
