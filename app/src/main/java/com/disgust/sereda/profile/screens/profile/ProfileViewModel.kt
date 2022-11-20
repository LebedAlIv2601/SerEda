package com.disgust.sereda.profile.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.profile.data.ProfileRepository
import com.disgust.sereda.profile.screens.profile.interaction.ProfileUIEvent
import com.disgust.sereda.profile.screens.profile.interaction.UserInfoState
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.navigation.navigateWithClearBackStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class ProfileViewModel @Inject constructor(val repository: ProfileRepository) : ViewModel(),
    UIEventHandler<ProfileUIEvent> {

    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Loading)
    val userInfoState = _userInfoState.asStateFlow()

    override fun onUIEvent(event: ProfileUIEvent) {
        when (event) {
            is ProfileUIEvent.StartScreen -> {
                getProfileData()
            }
            is ProfileUIEvent.ButtonSignOutClick -> {
                signOut(event.navController)
            }
        }
    }

    private fun signOut(navController: NavHostController) {
        doSingleRequest(
            query = { repository.signOut() },
            doOnSuccess = { navController.navigateWithClearBackStack(Screen.GoogleAuth.route) }
        )
    }

    private fun getProfileData() {
        doSingleRequest(
            query = { repository.getProfileInfo() },
            doOnSuccess = { _userInfoState.value = UserInfoState.Success(it) }
        )
    }
}