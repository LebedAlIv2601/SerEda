package com.disgust.sereda.auth.phone

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.auth.phone.interaction.PhoneEnterUIEvent
import com.disgust.sereda.auth.phone.interaction.RequestSendCodeState
import com.disgust.sereda.auth.phone.interaction.SendCodeState

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun PhoneEnterScreen(
    navController: NavHostController,
    vm: PhoneEnterViewModel = hiltViewModel()
) {
    val sendCodeState = vm.sendCodeState.collectAsState()
    val requestSendCodeState = vm.requestSendState.collectAsState()

    val inputText = vm.inputText.collectAsState()
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(
            value = inputText.value,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            onValueChange = { vm.onUIEvent(PhoneEnterUIEvent.InputTextChange(it)) }
        )
        when (sendCodeState.value) {
            is SendCodeState.Error -> Text(text = (sendCodeState.value as SendCodeState.Error).error)
            is SendCodeState.Success -> vm.onUIEvent(
                PhoneEnterUIEvent.SmsCodeSentSuccessfully(
                    navController
                )
            )
            else -> {}
        }
        when (requestSendCodeState.value) {
            is RequestSendCodeState.Error -> Text(text = (requestSendCodeState.value as RequestSendCodeState.Error).error)
            else -> {}
        }
        Button(onClick = {
            Log.e("9", "getCode called")
            vm.onUIEvent(PhoneEnterUIEvent.ButtonGetCodeClick)
        }
        ) {
            Text(text = "Get code")
        }
    }
}