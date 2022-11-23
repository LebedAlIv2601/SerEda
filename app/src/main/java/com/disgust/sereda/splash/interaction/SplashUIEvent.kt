package com.disgust.sereda.splash.interaction

import com.disgust.sereda.utils.base.BaseUIEvent

sealed class SplashUIEvent : BaseUIEvent {
    object StartScreen : SplashUIEvent()
}
