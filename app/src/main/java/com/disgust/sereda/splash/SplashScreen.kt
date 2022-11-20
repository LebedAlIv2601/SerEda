package com.disgust.sereda.splash

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.splash.interaction.SplashUIEvent
import com.disgust.sereda.utils.DoOnInit

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SplashScreen(
    navController: NavHostController,
    vm: SplashViewModel = hiltViewModel()
) {
    DoOnInit {
        vm.onUIEvent(SplashUIEvent.StartScreen(navController))
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight())
}