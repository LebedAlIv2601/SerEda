package com.disgust.sereda.auth.googleAuth.interaction

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class GoogleAuthUIEvent : BaseUIEvent {
    class ButtonAuthClick(
        val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
    ) : GoogleAuthUIEvent()

    class IntentResultOk(
        val intent: Intent?,
        val navController: NavHostController
    ) : GoogleAuthUIEvent()
}