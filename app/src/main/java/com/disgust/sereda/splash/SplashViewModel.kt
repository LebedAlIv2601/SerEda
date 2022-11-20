package com.disgust.sereda.splash

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import com.disgust.sereda.splash.data.SplashRepository
import com.disgust.sereda.splash.interaction.SplashUIEvent
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.navigation.navigateWithClearBackStack
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashRepository) :
    ViewModel(), UIEventHandler<SplashUIEvent> {
    override fun onUIEvent(event: SplashUIEvent) {
        when (event) {
            is SplashUIEvent.StartScreen -> {
                doSingleRequest(
                    query = { repository.isAuth() },
                    doOnSuccess = {
                        event.navController.navigateWithClearBackStack(
                            if (it) {
                                Screen.Screen1.route
                            } else {
                                Screen.GoogleAuth.route
                            }
                        )
                    }
                )
            }
        }
    }
}