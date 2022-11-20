package com.disgust.sereda.auth.code

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import com.disgust.sereda.auth.code.interaction.CodeEnterUIEvent
import com.disgust.sereda.auth.code.interaction.RequestVerifyCodeState
import com.disgust.sereda.auth.data.AuthRepository
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.navigation.navigateWithClearBackStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@HiltViewModel
class CodeEnterViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel(),
    UIEventHandler<CodeEnterUIEvent> {

    val codeVerificationState = repository.codeVerificationState
    private val _requestVerifyCodeState =
        MutableStateFlow<RequestVerifyCodeState>(RequestVerifyCodeState.NoErrors)
    val requestVerifyCodeState = _requestVerifyCodeState.asStateFlow()


    override fun onUIEvent(event: CodeEnterUIEvent) {
        when (event) {
            is CodeEnterUIEvent.ButtonEnterCodeClick -> {
                verifyCode(event.code)
            }
            is CodeEnterUIEvent.RightCodeEntered -> {
                event.navController.navigateWithClearBackStack(Screen.Screen1.route)
            }
        }
    }

    private fun verifyCode(code: String) {
        doSingleRequest(
            query = { repository.verifyCode(code = code) },
            doOnSuccess = { _requestVerifyCodeState.value = RequestVerifyCodeState.NoErrors },
            doOnError = {
                _requestVerifyCodeState.value = RequestVerifyCodeState.Error(
                    it.message ?: "Something went wrong. Please, try again"
                )
            },
            doOnLoading = {}
        )
    }

}