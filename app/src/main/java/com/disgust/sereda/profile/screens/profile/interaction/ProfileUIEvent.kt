package com.disgust.sereda.profile.screens.profile.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class ProfileUIEvent : BaseUIEvent {
    object StartScreen : ProfileUIEvent()
    class ButtonSignOutClick(val navController: NavHostController) : ProfileUIEvent()
}
