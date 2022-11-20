package com.disgust.sereda.auth.googleAuth

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.disgust.sereda.auth.googleAuth.interaction.GoogleAuthUIEvent
import com.google.android.gms.common.api.ApiException

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun GoogleAuthScreen(
    vm: GoogleAuthViewModel
) {
    val authDialogLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    vm.onUIEvent(GoogleAuthUIEvent.IntentResultOk(result.data))
                } catch (e: ApiException) {
                    print(e)
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            vm.onUIEvent(GoogleAuthUIEvent.ButtonAuthClick(launcher = authDialogLauncher))
        }
        ) {
            Text(text = "Auth with google")
        }

    }
}