package com.disgust.sereda.auth.phone.interaction

sealed class SendCodeState {
    object Loading : SendCodeState()
    class Error(val error: String) : SendCodeState()
    object Success : SendCodeState()
    object Waiting : SendCodeState()
}
