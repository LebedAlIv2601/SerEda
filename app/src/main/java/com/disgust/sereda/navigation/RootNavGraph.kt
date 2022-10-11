package com.disgust.sereda.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.disgust.sereda.utils.createDestination

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Screen1.route
    ) {
        createDestination(Screen.Screen1, navController)
        createDestination(Screen.Screen2, navController)
    }
}
