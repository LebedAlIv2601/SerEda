package com.disgust.sereda.auth.phone.interaction

sealed class RequestSendCodeState {
    class Error(val error: String) : RequestSendCodeState()
    object NoErrors : RequestSendCodeState()
}
