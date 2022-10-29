package com.disgust.sereda.auth.code.interaction

sealed class RequestVerifyCodeState {
    object NoErrors : RequestVerifyCodeState()
    class Error(val error: String) : RequestVerifyCodeState()
}
