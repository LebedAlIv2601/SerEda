package com.disgust.sereda.profile.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
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

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class ProfileViewModel @Inject constructor(val repository: ProfileRepository) :
    NavigatorViewModel(),
    UIEventHandler<ProfileUIEvent> {

    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Waiting)
    val userInfoState = _userInfoState.asStateFlow()

    override fun onUIEvent(event: ProfileUIEvent) {
        when (event) {
            is ProfileUIEvent.StartScreen -> {
                if (isAuth()) {
                    getProfileData()
                } else {
                    _userInfoState.value = UserInfoState.NotAuth
                }
            }
            is ProfileUIEvent.ButtonSignOutClick -> {
                signOut()
            }
            ProfileUIEvent.ButtonAuthClick -> {
                navigate(Screen.GoogleAuth.route)
            }
        }
    }

    private fun isAuth() = repository.isAuth()

    private fun signOut() {
        doSingleRequest(
            query = { repository.signOut() },
            doOnSuccess = { navigateWithClearBackStack(Screen.SearchRecipe.route) },
        )
    }

    private fun getProfileData() {
        doSingleRequest(
            query = { repository.getProfileInfo() },
            doOnSuccess = { _userInfoState.value = UserInfoState.Success(it) },
            doOnLoading = { _userInfoState.value = UserInfoState.Loading }
        )
    }
}