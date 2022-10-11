package com.disgust.sereda.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.disgust.sereda.utils.createDestination
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalAnimationApi
@Composable
fun RootNavGraph(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Screen1.route
    ) {
        createDestination(Screen.Screen1, navController)
        createDestination(Screen.Screen2, navController)
    }
}
