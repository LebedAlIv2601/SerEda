package com.disgust.sereda.auth.phone.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class PhoneEnterUIEvent : BaseUIEvent {
    object ButtonGetCodeClick : PhoneEnterUIEvent()
    class SmsCodeSentSuccessfully(val navController: NavHostController) : PhoneEnterUIEvent()
    class InputTextChange(val text: String) : PhoneEnterUIEvent()
}