package com.disgust.sereda.auth.code.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class CodeEnterUIEvent : BaseUIEvent {
    class ButtonEnterCodeClick(val code: String) : CodeEnterUIEvent()
    class RightCodeEntered(val navController: NavHostController) : CodeEnterUIEvent()
}