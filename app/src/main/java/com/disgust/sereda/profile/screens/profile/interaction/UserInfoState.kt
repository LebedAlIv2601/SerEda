package com.disgust.sereda.profile.screens.profile.interaction

import com.disgust.sereda.profile.screens.profile.model.ProfileUser
import com.disgust.sereda.utils.base.BaseState

sealed class UserInfoState : BaseState() {
    object Waiting : UserInfoState()
    object Loading : UserInfoState()
    class Success(val userInfo: ProfileUser) : UserInfoState()
    class Error(val e: Exception) : UserInfoState()
    object NotAuth : UserInfoState()
}
