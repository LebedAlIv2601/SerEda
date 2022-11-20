package com.disgust.sereda.splash

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.disgust.sereda.splash.interaction.SplashUIEvent
import com.disgust.sereda.utils.DoOnInit

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SplashScreen(
    vm: SplashViewModel
) {
    DoOnInit {
        vm.onUIEvent(SplashUIEvent.StartScreen)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}