package com.disgust.sereda.profile.screens.profile.interaction

import com.disgust.sereda.profile.screens.profile.model.ProfileUser

sealed class UserInfoState {
    object Loading : UserInfoState()
    class Success(val userInfo: ProfileUser) : UserInfoState()
    class Error(val e: Exception) : UserInfoState()
}
