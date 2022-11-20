package com.disgust.sereda.profile.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.profile.data.ProfileRepository
import com.disgust.sereda.profile.screens.profile.interaction.ProfileUIEvent
import com.disgust.sereda.profile.screens.profile.interaction.UserInfoState
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class ProfileViewModel @Inject constructor(val repository: ProfileRepository) :
    NavigatorViewModel(),
    UIEventHandler<ProfileUIEvent> {

    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Loading)
    val userInfoState = _userInfoState.asStateFlow()

    override fun onUIEvent(event: ProfileUIEvent) {
        when (event) {
            is ProfileUIEvent.StartScreen -> {
                getProfileData()
            }
            is ProfileUIEvent.ButtonSignOutClick -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        doSingleRequest(
            query = { repository.signOut() },
            doOnSuccess = { navigateWithClearBackStack(Screen.GoogleAuth.route) }
        )
    }

    private fun getProfileData() {
        doSingleRequest(
            query = { repository.getProfileInfo() },
            doOnSuccess = { _userInfoState.value = UserInfoState.Success(it) }
        )
    }
}