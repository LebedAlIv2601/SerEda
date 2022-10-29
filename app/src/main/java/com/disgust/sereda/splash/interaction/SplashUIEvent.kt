package com.disgust.sereda.splash.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.BaseUIEvent

sealed class SplashUIEvent : BaseUIEvent {
    class StartScreen(val navController: NavHostController) : SplashUIEvent()
}
