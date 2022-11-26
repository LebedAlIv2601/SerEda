package com.disgust.sereda.auth.googleAuth.interaction

import com.disgust.sereda.utils.base.BaseState
import com.google.android.gms.auth.api.identity.BeginSignInResult

sealed class OneTapSignInState : BaseState() {
    object Waiting : OneTapSignInState()
    object Loading : OneTapSignInState()
    class Success(val signInResult: BeginSignInResult) : OneTapSignInState()
}
