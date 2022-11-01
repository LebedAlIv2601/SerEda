package com.disgust.sereda.auth.data

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.auth.code.interaction.CodeVerificationState
import com.disgust.sereda.auth.phone.interaction.SendCodeState
import com.disgust.sereda.utils.FirebaseHelper
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseHelper: FirebaseHelper) {

    private val _sendCodeState = MutableStateFlow<SendCodeState>(SendCodeState.Waiting)
    val sendCodeState = _sendCodeState.asStateFlow()
    private val _codeVerificationState =
        MutableStateFlow(CodeVerificationState.WAITING)
    val codeVerificationState = _codeVerificationState.asStateFlow()

    fun clearSendCodeState() {
        _sendCodeState.value = SendCodeState.Waiting
    }

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    fun getCode(phone: String) {
        _sendCodeState.value = SendCodeState.Loading
        firebaseHelper.getCode(
            phone = phone,
            onVerificationFailed = { e ->
                if (e is FirebaseAuthInvalidCredentialsException) {
                    _sendCodeState.value =
                        SendCodeState.Error("Something went wong. Please, try again")
                } else if (e is FirebaseTooManyRequestsException) {
                    _sendCodeState.value =
                        SendCodeState.Error("Something went wong. Please, try again later")
                }
            },
            onCodeSent = { _, _ ->
                _sendCodeState.value = SendCodeState.Success
            }
        )
    }

    fun verifyCode(code: String) =
        firebaseHelper.verifyCode(code = code, completeListener = { task ->
            _codeVerificationState.value = if (task.isSuccessful) {
                CodeVerificationState.RIGHT
            } else {
                CodeVerificationState.WRONG
            }
        })

}