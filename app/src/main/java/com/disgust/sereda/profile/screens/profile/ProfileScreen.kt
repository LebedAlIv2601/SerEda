package com.disgust.sereda.profile.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.profile.screens.profile.interaction.ProfileUIEvent
import com.disgust.sereda.profile.screens.profile.interaction.UserInfoState
import com.disgust.sereda.utils.DoOnInit

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ProfileScreen(
    navController: NavHostController,
    vm: ProfileViewModel = hiltViewModel()
) {

    val userInfoState = vm.userInfoState.collectAsState()

    DoOnInit {
        vm.onUIEvent(ProfileUIEvent.StartScreen)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val userStateValue = userInfoState.value) {
            is UserInfoState.Loading -> {
                CircularProgressIndicator()
            }
            is UserInfoState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = userStateValue.userInfo.toString())
                    Button(onClick = { vm.onUIEvent(ProfileUIEvent.ButtonSignOutClick(navController)) }) {
                        Text(text = "Sign Out")
                    }
                }
            }
            is UserInfoState.Error -> {
                Text(text = userStateValue.e.toString())
            }
        }
    }
}