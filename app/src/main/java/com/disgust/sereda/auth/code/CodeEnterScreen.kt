package com.disgust.sereda.auth.code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.auth.code.interaction.CodeEnterUIEvent
import com.disgust.sereda.auth.code.interaction.CodeVerificationState
import com.disgust.sereda.auth.code.interaction.RequestVerifyCodeState

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CodeEnterScreen(
    navController: NavHostController,
    vm: CodeEnterViewModel = hiltViewModel()
) {

    val codeVerificationState = vm.codeVerificationState.collectAsState()
    val requestVerifyCodeState = vm.requestVerifyCodeState.collectAsState()

    val inputText = remember { mutableStateOf("") }
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
            onValueChange = { inputText.value = it }
        )
        when (codeVerificationState.value) {
            CodeVerificationState.WRONG -> Text(text = "Неверный код")
            CodeVerificationState.RIGHT -> vm.onUIEvent(
                CodeEnterUIEvent.RightCodeEntered(
                    navController
                )
            )
            else -> {}
        }
        when (requestVerifyCodeState.value) {
            is RequestVerifyCodeState.Error ->
                Text(text = (requestVerifyCodeState.value as RequestVerifyCodeState.Error).error)
            else -> {}
        }
        Button(onClick = {
            vm.onUIEvent(CodeEnterUIEvent.ButtonEnterCodeClick(code = inputText.value))
        }
        ) {
            Text(text = "Verify code")
        }
    }
}