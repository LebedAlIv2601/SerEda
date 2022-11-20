package com.disgust.sereda.profile.screens.profile.interaction

import com.disgust.sereda.utils.base.BaseUIEvent

sealed class ProfileUIEvent : BaseUIEvent {
    object StartScreen : ProfileUIEvent()
    object ButtonSignOutClick : ProfileUIEvent()
}
