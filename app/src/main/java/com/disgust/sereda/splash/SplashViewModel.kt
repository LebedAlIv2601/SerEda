package com.disgust.sereda.splash

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.splash.data.SplashRepository
import com.disgust.sereda.splash.interaction.SplashUIEvent
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashRepository) :
    NavigatorViewModel(), UIEventHandler<SplashUIEvent> {
    override fun onUIEvent(event: SplashUIEvent) {
        when (event) {
            is SplashUIEvent.StartScreen -> {
                doSingleRequest(
                    query = { repository.isAuth() },
                    doOnSuccess = {
                        navigateWithClearBackStack(
                            if (it) {
                                Screen.SearchRecipe.route
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