package com.disgust.sereda.auth.phone

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import com.disgust.sereda.auth.data.AuthRepository
import com.disgust.sereda.auth.phone.interaction.PhoneEnterUIEvent
import com.disgust.sereda.auth.phone.interaction.RequestSendCodeState
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class PhoneEnterViewModel @Inject constructor(private val repository: AuthRepository) :
    ViewModel(), UIEventHandler<PhoneEnterUIEvent> {

    val sendCodeState = repository.sendCodeState

    private val _requestSendState =
        MutableStateFlow<RequestSendCodeState>(RequestSendCodeState.NoErrors)
    val requestSendState = _requestSendState.asStateFlow()

    private val _inputText =
        MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    override fun onUIEvent(event: PhoneEnterUIEvent) {
        when (event) {
            is PhoneEnterUIEvent.ButtonGetCodeClick -> {
                getCode(_inputText.value)
            }
            is PhoneEnterUIEvent.SmsCodeSentSuccessfully -> {
                repository.clearSendCodeState()
                event.navController.navigate(Screen.CodeEnter.route)
            }
            is PhoneEnterUIEvent.InputTextChange -> {
                _inputText.value = event.text
            }
        }
    }

    private fun getCode(phone: String) {
        doSingleRequest(
            query = { repository.getCode(phone) },
            doOnSuccess = { _requestSendState.value = RequestSendCodeState.NoErrors },
            doOnLoading = {},
            doOnError = {
                _requestSendState.value = RequestSendCodeState.Error(
                    it.message ?: "Something went wrong. Please, try again"
                )
            }
        )
    }
}