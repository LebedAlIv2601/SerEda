package com.disgust.sereda.profile.screens.profile.interaction

import com.disgust.sereda.utils.base.BaseUIEvent
import com.disgust.sereda.utils.commonModel.Diet
import com.disgust.sereda.utils.commonModel.Intolerance

sealed class ProfileUIEvent : BaseUIEvent {
    object StartScreen : ProfileUIEvent()
    object ButtonSignOutClick : ProfileUIEvent()
    object ButtonAuthClick : ProfileUIEvent()
    class FiltersSetDiet(val diet: Diet, val isAdd: Boolean) : ProfileUIEvent()
    class FiltersSetIntolerance(val intolerance: Intolerance, val isAdd: Boolean) :
        ProfileUIEvent()
}
